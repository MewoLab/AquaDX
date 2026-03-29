package aquadx.net.games

import com.fasterxml.jackson.annotation.JsonIgnore
import ext.JACKSON
import ext.JavaSerializable
import aquadx.sega.general.model.Card
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import kotlinx.serialization.Serializable

data class TrendOut(val date: String, val rating: Int, val plays: Int)

data class RankCount(val name: String, val count: Int)

data class GenericGameSummary(
    val name: String,
    val aquaUser: Map<String, Any?>?,
    val serverRank: Long,
    val accuracy: Double,
    val rating: Int,
    val ratingHighest: Int,
    val ranks: List<RankCount>,
    val detailedRanks: Map<Int, Map<String, Int>>,
    val maxCombo: Int,
    val fullCombo: Int,
    val allPerfect: Int,
    val totalScore: Long,
    val plays: Int,
    val totalPlayTime: Long,
    val joined: String,
    val lastSeen: String,
    val lastVersion: String,
    val lastPlayedHost: String? = null,
    val ratingComposition: Map<String, Any>,
    val recent: List<IGenericGamePlaylog>,
    val rival: Boolean?,
    val favorites: List<Int>?
)

data class GenericRankingPlayer(
    var rank: Int,
    val name: String,
    val username: String?,
    val accuracy: Double,
    val rating: Int,
    val allPerfect: Int,
    val fullCombo: Int,
    val lastSeen: String
)

@Serializable
data class GenericMusicMeta(
    val name: String?,
    val ver: String,
    val notes: List<GenericNoteMeta>
)

@Serializable
data class GenericNoteMeta(
    val lv: Double?,
)

@Serializable
data class GenericItemMeta(
    val name: String? = null,
    val disable: Boolean? = null,
    val ver: String? = null
)

// Here are some interfaces to generalize across multiple games
interface IUserData {
    val id: Long
    var userName: String
    val playerRating: Int
    val highestRating: Int
    val firstPlayDate: Any
    val lastPlayDate: Any
    val lastRomVersion: String
    val totalScore: Long
    var card: Card?
    val lastClientId: String?
}

interface IGenericGamePlaylog {
    val user: IUserData
    val musicId: Int
    val level: Int
    val userPlayDate: Any
    val achievement: Int
    val maxCombo: Int
    val isFullCombo: Boolean
    val beforeRating: Int
    val afterRating: Int
    val isAllPerfect: Boolean
}

interface IGenericUserMusic {
    val musicId: Int
}

interface IUserEntity<UserModel: IUserData> {
    var id: Long
    var user: UserModel
}

interface IExportClass<UserModel: IUserData> {
    var gameId: String
    var userData: UserModel
}

@MappedSuperclass
open class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    open var id: Long = 0
) : JavaSerializable {
    override fun toString() = JACKSON.writeValueAsString(this)
}

data class ImportResult(val errors: List<String>, val warnings: List<String>, val json: String)
