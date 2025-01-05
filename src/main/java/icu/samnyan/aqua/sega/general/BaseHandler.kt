package icu.samnyan.aqua.sega.general

import com.fasterxml.jackson.core.JsonProcessingException
import ext.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.scheduling.annotation.Scheduled


fun interface BaseHandler {
    @Throws(JsonProcessingException::class)
    fun handle(request: Map<String, Any>): Any?
}

const val MAX_COUNT = Int.MAX_VALUE / 2
class RequestContext(
    val req: HttpServletRequest,
    val data: MutableMap<String, Any>,
) {
    val uid by lazy { parsing { data["userId"]!!.long } }
    val nextIndex by lazy { parsing { data["nextIndex"]?.int ?: -1 } }
    val maxCount by lazy { parsing { data["maxCount"]?.int ?: MAX_COUNT } }
}

typealias SpecialHandler = RequestContext.() -> Any?
fun BaseHandler.toSpecial() = { ctx: RequestContext -> handle(ctx.data) }

typealias PagedHandler = RequestContext.() -> List<Any>
typealias AddFn = RequestContext.() -> PagedProcessor
typealias PagePost = (MutJDict) -> Unit
data class PagedProcessor(val add: JDict?, val fn: PagedHandler, var post: PagePost? = null)

// A very :3 way of declaring APIs
abstract class MeowApi(val serialize: (String, Any?) -> String) {
    val initH = mutableMapOf<String, SpecialHandler>()
    infix operator fun String.invoke(fn: SpecialHandler) = initH.set("${this}Api", fn)
    infix fun String.static(fn: () -> Any) = serialize(this, fn()).let { resp -> this { resp } }

    // Page Cache: {cache key: (timestamp, full list)}
    val pageCache = mutableMapOf<String, Pair<Long, List<Any>>>()

    fun String.pagedWithKind(key: String, addFn: AddFn) = this api@ {
        val p = addFn()
        val add = p.add ?: emptyMap()

        if (nextIndex == -1) return@api p.fn(this).let { mapOf("userId" to uid, "length" to it.size, key to it) + add }

        // Try to get cache
        val cacheKey = "$key:$uid:$add"
        val list = pageCache.getOrPut(cacheKey) { p.fn(this).let {
            if (it.size > maxCount) millis() to it
            else return@api mapOf("userId" to uid, "length" to it.size, "nextIndex" to -1, key to it) + add
        } }.r

        // Get sublist and next index
        var iAfter = (nextIndex + maxCount).coerceAtMost(list.size)
        val lst = list.slice(nextIndex until iAfter)

        // Update cache if needed
        if (iAfter == list.size) {
            pageCache.remove(cacheKey)
            iAfter = -1
        }
        else pageCache[cacheKey] = millis() to list

        (mapOf("userId" to uid, "length" to lst.size, "nextIndex" to iAfter, key to lst) + add).mut
            .also { p.post?.invoke(it) }
    }
    fun String.paged(key: String, fn: PagedHandler) = pagedWithKind(key) { PagedProcessor(null, fn) }
    infix fun JDict.grabs(fn: PagedHandler) = PagedProcessor(this, fn, null)
    infix fun PagedProcessor.postProcess(post: PagePost) = also { it.post = post }

    // Page cache cleanup every minute
    @Scheduled(fixedRate = (1000 * 60))
    fun cleanupCache() {
        val minTime = millis() - (1000 * 60)
        pageCache.entries.removeIf { it.value.l < minTime }
    }
}
