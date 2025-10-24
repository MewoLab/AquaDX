package icu.samnyan.aqua.sega.general.filter

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.ByteArrayInputStream

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
class CompressRequestWrapper(request: HttpServletRequest, input: ByteArray) : HttpServletRequestWrapper(request) {
    val input: ByteArrayInputStream = ByteArrayInputStream(input)
    var filterInput: ServletInputStream? = null

    override fun getInputStream(): ServletInputStream {
        return filterInput ?: object : ServletInputStream() {
            override fun isFinished() = false
            override fun isReady() = false
            override fun setReadListener(readListener: ReadListener) {}
            override fun read() = input.read()
        }.also { filterInput = it }
    }
}
