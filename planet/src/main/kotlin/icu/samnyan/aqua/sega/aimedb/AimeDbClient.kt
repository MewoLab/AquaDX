package icu.samnyan.aqua.sega.aimedb

import ext.minus
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import java.net.Socket
import java.net.SocketTimeoutException

class AimeDbClient(val gameId: String, val keychipShort: String, val server: String) {
    // https://sega.bsnk.me/allnet/aimedb/common/#packet-header
    fun createRequest(type: UShort, writer: ByteBuf.() -> Unit) =
        AimeDbEncryption.encrypt(Unpooled.buffer(1024).clear().run {
            writeShortLE(0xa13e)            // 00  2b: Magic
            writeShortLE(0x3087)            // 02  2b: Version
            writeShortLE(type.toInt())      // 04  2b: Type
            writeShortLE(0)                 // 06  2b: Length
            writeShortLE(0)                 // 08  2b: Result
            writeAscii(gameId, 6)           // 0A  6b: Game ID
            writeIntLE(299)                 // 10  4b: Store ID (Place ID)
            writeAscii(keychipShort, 12)    // 14 12b: Keychip ID
            writer()                        // Write Payload
            setShortLE(6, writerIndex())    // Update Length
            copy(0, writerIndex())          // Trim unused bytes
        })

    private fun ByteBuf.writeAscii(value: String, length: Int) =
        writeBytes(value.toByteArray(Charsets.US_ASCII).copyOf(length))

    fun createReqLookupV2(accessCode: String) =
        createRequest(0x0fu) {
            // Access code is a 20-digit number, should be converted to a 10-byte array
            writeBytes(ByteBufUtil.decodeHexDump(accessCode.padStart(20, '0')))
            writeByte(0)  // 0A  1b: Company code
            writeByte(0)  // 0B  1b: R/W Firmware version
            writeIntLE(0) // 0C  4b: Serial number
        }

    fun createReqFelicaLookupV2(felicaIdm: String) =
        createRequest(0x11u) {
            writeBytes(ByteArray(16))   // 00 16b: Random Challenge
            // 10  8b: Felica IDm
            writeBytes(ByteBufUtil.decodeHexDump(felicaIdm.padStart(16, '0')))
            writeBytes(ByteArray(8))    // 18  8b: Felica PMm
            writeBytes(ByteArray(16))   // 20 16b: Card key version
            writeBytes(ByteArray(16))   // 30 16b: Write count
            writeBytes(ByteArray(8))    // 40  8b: MACA
            writeByte(0)                // 48  1b: Company code
            writeByte(0)                // 49  1b: R/W Firmware version
            writeShortLE(0)             // 4A  2b: DFC
            writeIntLE(0)               // 4C  4b: Unknown padding
        }

    fun createReqRegister(accessCode: String) =
        createRequest(0x05u) {
            // Access code is a 20-digit number, should be converted to a 10-byte array
            writeBytes(ByteBufUtil.decodeHexDump(accessCode.padStart(20, '0')))
            writeByte(0)  // 0A  1b: Company code
            writeByte(0)  // 0B  1b: R/W Firmware version
            writeIntLE(0) // 0C  4b: Serial number
        }

    fun send(buf: ByteBuf): ByteBuf = Socket(server, 22345).use {
        it.soTimeout = 3000
        it.getOutputStream().write(buf.array())
        it.getInputStream().use { r ->
            Unpooled.buffer().apply {
                val buffer = ByteArray(1024)
                try {
                    while (r.read(buffer) != -1) writeBytes(buffer)
                } catch (_: SocketTimeoutException) { }
            }
        }
    }.let { AimeDbEncryption.decrypt(it) }

    fun execLookup(card: String) =
        send(when (card.length) {
            20 -> createReqLookupV2(card)
            16 -> createReqFelicaLookupV2(card)
            else -> 400 - "Invalid card. Please input either 20-digit numeric access code (e.g. 5010000...0) or 16-digit hex Felica ID (e.g. 012E123456789ABC)."
        }).getUnsignedIntLE(0x20).let {
            if (it == 0xffffffff) -1L else it
        }

    fun execRegister(card: String) =
        when (card.length) {
            20 -> card
            16 -> ByteBufUtil.hexDump(send(createReqFelicaLookupV2(card)).slice(0x2c, 10))
            else -> 400 - "Invalid card. Please input a 20-digit numeric access code (e.g. 5010000...0)."
        }.let { send(createReqRegister(it)).getUnsignedIntLE(0x20) }

    fun execLookupOrRegister(card: String) =
        execLookup(card).let {
            if (it == -1L) execRegister(card)
            else it
        }
}
