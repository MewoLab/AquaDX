package icu.samnyan.aqua.sega.util

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

object ZLib {
    fun decompress(src: ByteArray, nowrap: Boolean = false) = Inflater(nowrap).run {
        val buffer = ByteArray(1024)
        setInput(src)
        ByteArrayOutputStream().use {
            var count = -1
            while (count != 0) {
                count = inflate(buffer)
                it.write(buffer, 0, count)
            }
            end()
            it.toByteArray()
        }
    }

    fun compress(src: ByteArray) = Deflater().run {
        setInput(src)
        finish()

        val outputBuf = ByteArray(src.size * 4)
        val compressedSize = deflate(outputBuf)
        end()

        outputBuf.copyOf(compressedSize)
    }
}
