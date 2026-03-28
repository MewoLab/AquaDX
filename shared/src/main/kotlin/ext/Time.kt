package ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.*

// Date and time
val JST_ZONE = ZoneId.of("Asia/Tokyo")
fun jstNow() = LocalDateTime.now(JST_ZONE)
fun millis() = System.currentTimeMillis()
fun utcNow() = LocalDateTime.now(UTC)
val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
fun LocalDate.isoDate() = format(DATE_FORMAT)
fun String.isoDate() = DATE_FORMAT.parse(this, LocalDate::from)
fun Date.utc() = toInstant().atZone(UTC).toLocalDate()
fun LocalDate.toDate() = Date(atStartOfDay().toInstant(UTC).toEpochMilli())
fun LocalDateTime.isoDateTime() = format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
fun String.isoDateTime() = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
val URL_SAFE_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
fun LocalDateTime.urlSafeStr() = format(URL_SAFE_DT)
val DATE_2018 = LocalDateTime.parse("2018-01-01T00:00:00")

val ALT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
fun Str.asDateTime() = try { LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
catch (e: Exception) { try { LocalDateTime.parse(this, ALT_DATETIME_FORMAT) }
catch (e: Exception) { null } }

val Calendar.year get() = get(Calendar.YEAR)
val Calendar.month get() = get(Calendar.MONTH) + 1
val Calendar.day get() = get(Calendar.DAY_OF_MONTH)
fun cal() = Calendar.getInstance()
fun Date.cal() = Calendar.getInstance().apply { time = this@cal }
operator fun Calendar.invoke(field: Int) = get(field)
val Date.sec get() = time / 1000
