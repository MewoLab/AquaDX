package icu.samnyan.aqua.net.transfer

import ext.header
import ext.post
import ext.request
import java.net.URI
import icu.samnyan.aqua.sega.aimedb.AimeDbClient
import icu.samnyan.aqua.sega.allnet.AllNetBillingDecoder
import icu.samnyan.aqua.sega.allnet.AllNetBillingDecoder.decodeAllNetResp

val keychipPattern = Regex("([A-Z\\d]{4}-[A-Z\\d]{11}|[A-Z\\d]{11})")

class AllNetClient(val dns: String, val keychip: String, val game: String, val version: String, val card: String) {
    init {
        // Check if keychip is valid
        // TODO : Use a more appropriate exception
        if (!keychipPattern.matches(keychip)) throw Exception("Invalid keychip")
    }

    override fun toString() = "AllNetClient($dns, $keychip, $game, $version, $card)"

    val keychipShort by lazy {
        // A123-45678901337 -> A1234567890
        if (keychip.length == 11) keychip
        else keychip.substring(0, 4) + keychip.substring(5, 12)
    }
    val aime by lazy { AimeDbClient(game, keychipShort, URI(dns).host) }

    // Send AllNet PowerOn request to obtain game URL
    val gameUrl by lazy {
        "$dns/sys/servlet/PowerOn".request()
            .header("User-Agent" to "AquaTrans/1.0")
            .header("Content-Type" to "application/x-www-form-urlencoded")
            .header("Pragma" to "DFI")
            .post(AllNetBillingDecoder.encodeAllNet(mapOf(
                "game_id" to game,
                "ver" to version,
                "serial" to keychipShort,
                "ip" to "127.0.0.1", "firm_ver" to "60001", "boot_ver" to "0000",
                "encode" to "UTF-8", "format_ver" to "3", "hops" to "1", "token" to "2864179931"
            )))
            ?.decodeAllNetResp()?.get("uri")
            ?: throw Exception("PowerOn Failed: No game URL returned")
    }

    val userId by lazy { aime.execLookupOrRegister(card) }

    fun findDataBroker(log: (String) -> Unit) = when (game) {
        "SDHD" -> ChusanDataBroker(this, log)
        "SDEZ", "SDGA" -> MaimaiDataBroker(this, log)
        "SDDT" -> OngekiDataBroker(this, log)
        else -> throw IllegalArgumentException("Unsupported game: $game")
    }
}