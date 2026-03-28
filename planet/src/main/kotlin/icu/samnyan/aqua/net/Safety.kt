package icu.samnyan.aqua.net

import ext.HTTP
import ext.mut
import ext.toJson
import icu.samnyan.aqua.net.games.BaseEntity
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import jakarta.persistence.Entity
import kotlinx.serialization.Serializable
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.text.Normalizer

@Configuration
@ConfigurationProperties(prefix = "aqua-net.openai")
class OpenAIConfig {
    var apiKey: String = ""
}

@Entity
class AquaNetSafety : BaseEntity() {
    var content: String = ""
    var safe: Boolean = false
}

interface AquaNetSafetyRepo : JpaRepository<AquaNetSafety, Long> {
    fun findByContent(content: String): AquaNetSafety?
}

@Serializable
data class OpenAIResp<T>(
    val id: String,
    val model: String,
    val results: List<T>
)

@Serializable
data class OpenAIMod(
    val flagged: Boolean,
    val categories: Map<String, Boolean>,
    val categoryScores: Map<String, Double>,
)

@Service
class AquaNetSafetyService(
    val safety: AquaNetSafetyRepo,
    val openAIConfig: OpenAIConfig
) {
    /**
     * It is very inefficient to have query inside a loop, so we batch the query.
     */
    suspend fun isSafeBatch(rawContents: List<String>): List<Boolean> {
        val contents = rawContents.map { Normalizer.normalize(it, Normalizer.Form.NFKC) }
        val origMap = safety.findAll().associateBy { it.content }.mut
        val map = safety.findAll().associateBy { it.content.lowercase().trim() }.mut

        // Process unseen content with OpenAI
        val news = contents.filter { it.lowercase().trim() !in map && it !in contents }.map { inp ->
            HTTP.post("https://api.openai.com/v1/moderations") {
                header("Authorization", "Bearer ${openAIConfig.apiKey}")
                header("Content-Type", "application/json")
                setBody(mapOf("input" to inp).toJson())
            }.let {
                if (!it.status.isSuccess()) throw Exception("OpenAI request failed for $inp")
                val body = it.body<OpenAIResp<OpenAIMod>>()
                AquaNetSafety().apply {
                    content = inp
                    safe = !body.results.first().flagged
                }
            }
        }
        if (news.isNotEmpty()) safety.saveAll(news)
        news.associateByTo(origMap) { it.content }
        news.associateByTo(map) { it.content.lowercase().trim() }

        return contents.map { map[it.lowercase().trim()]?.safe ?: origMap[it]?.safe ?: true }
    }
}
