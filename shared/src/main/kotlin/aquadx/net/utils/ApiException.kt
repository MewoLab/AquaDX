package aquadx.net.utils

import ext.Str
import org.slf4j.LoggerFactory

class ApiException(val code: Int, message: Str) : RuntimeException(message) {
    companion object {
        val log = LoggerFactory.getLogger(ApiException::class.java)
    }
}
