package icu.samnyan.aqua.sega.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class GameEncryptionKey {
    var code: String? = null
    var versions: List<Int> = listOf()
    var key: String? = null
    var salt: String? = null
    var iv: String? = null
    var iterations = 0
}