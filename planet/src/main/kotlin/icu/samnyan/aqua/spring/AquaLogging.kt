package icu.samnyan.aqua.spring

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.CompositeConverter
import ext.get
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.ansi.AnsiOutput

private const val PROJECT_PACKAGE_PREFIX = "icu.samnyan.aqua"
private const val SEGA_PACKAGE_PREFIX = "icu.samnyan.aqua.sega"
private val SYSTEM_COLOR = AnsiColor.WHITE
private val SEGA_COLOR = AnsiColor.BRIGHT_CYAN
private val MISC_COLOR = AnsiColor.BRIGHT_BLUE

class LoggerComponent : CompositeConverter<ILoggingEvent>() {
    override fun transform(event: ILoggingEvent, input: String): String {
        val (str, clr) = if (event.loggerName.startsWith(PROJECT_PACKAGE_PREFIX)) {
            val sub = event.loggerName.substring(PROJECT_PACKAGE_PREFIX.length + 1)
            if (sub.startsWith("sega")) sub.substring(5).substringBefore(".")[0, 6].padEnd(6).uppercase() to SEGA_COLOR
            else sub.substringBefore(".")[0, 6].padEnd(6).uppercase() to MISC_COLOR
        } else "SYSTEM" to SYSTEM_COLOR
        return AnsiOutput.toString(clr, str)
    }
}

class LoggerClassColor : CompositeConverter<ILoggingEvent>() {
    override fun transform(event: ILoggingEvent, rawInput: String): String {
        var input = rawInput
        val clr = when {
            event.loggerName.startsWith(SEGA_PACKAGE_PREFIX) -> {
                input = event.loggerName.substring(SEGA_PACKAGE_PREFIX.length + 1)
                SEGA_COLOR
            }
            event.loggerName.startsWith(PROJECT_PACKAGE_PREFIX) -> {
                input = event.loggerName.substring(PROJECT_PACKAGE_PREFIX.length + 1)
                MISC_COLOR
            }
            else -> SYSTEM_COLOR
        }
        input = input[0, 40].padEnd(40)
        return AnsiOutput.toString(clr, input)
    }
}