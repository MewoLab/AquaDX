package icu.samnyan.aqua.sega.chusan

import ext.logger
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import org.springframework.stereotype.Component


@Component
class ChusanVersionHelper(val db: Chu3Repos) {
    val log = logger()

    // Cache of <client id : version>
    val cache: MutableMap<String, String> = mutableMapOf()

    // Obtain the cabinet's most recent version
    fun get(clientId: String): String? {
        // Try to find the version in the cache
        cache[clientId]?.let { return it }

        // Not found, check the most recent user
        return db.userData.findTopByLastClientIdOrderByLastPlayDateDesc(clientId)?.lastDataVersion
            ?.also { cache[clientId] = it }
    }

    fun set(clientId: String, version: String) {
        cache[clientId] = version
    }
}