package icu.samnyan.aqua.sega.aimedb

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled.copiedBuffer
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun ByteBuf.toBytes(): ByteArray {
    val readerPos = readerIndex()
    resetReaderIndex()
    val result = ByteArray(readableBytes())
    readBytes(result)
    readerIndex(readerPos)
    return result
}

fun ByteBuf.toAllBytes(): ByteArray {
    val readerPos = readerIndex()
    resetReaderIndex()
    writerIndex(capacity())
    val result = ByteArray(capacity())
    readBytes(result)
    readerIndex(readerPos)
    return result
}

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
object AimeDbEncryption {
    val KEY = SecretKeySpec("Copyright(C)SEGA".toByteArray(StandardCharsets.UTF_8), "AES")
    val enc = Cipher.getInstance("AES/ECB/NoPadding").apply { init(Cipher.ENCRYPT_MODE, KEY) }
    val dec = Cipher.getInstance("AES/ECB/NoPadding").apply { init(Cipher.DECRYPT_MODE, KEY) }

    fun decrypt(src: ByteBuf) = copiedBuffer(dec.doFinal(src.toBytes()))
    fun encrypt(src: ByteBuf) = copiedBuffer(enc.doFinal(src.toAllBytes()))
}
