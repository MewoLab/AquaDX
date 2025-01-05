package icu.samnyan.aqua.net.games

import ext.isoDate
import ext.minus
import ext.mut
import java.time.LocalDate

const val LETTERS = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" +
    "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ" +
    "０１２３４５６７８９"
const val SYMBOLS = "・：；？！～／＋－×÷＝♂♀∀＃＆＊＠☆○◎◇□△▽♪†‡ΣαβγθφψωДё＄（）．＿␣"
const val KANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん" +
    "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン"
const val SEGA_USERNAME_CAHRS = LETTERS + SYMBOLS + KANA
const val WACCA_USERNAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "abcdefghijklmnopqrstuvwxyz" +
    "0123456789" +
    "~|?!=()[]{},.:;\"@/_-+#*&%$"

fun usernameCheck(chars: String): (IUserData, String) -> Unit = { u, v ->
    u.userName = v
    if (v.isBlank()) { 400 - "Username cannot be blank" }
    if (v.length > 8) { 400 - "Username too long" }
    v.find { it !in chars }?.let { 400 - "Invalid character '$it' in username" }
}

fun toFullWidth(input: String): String {
    val stringBuilder = StringBuilder()
    for (char in input.toCharArray()) {
        if (char.code in 33..126) {
            stringBuilder.append((char.code + 65248).toChar())
        } else {
            stringBuilder.append(char)
        }
    }
    return stringBuilder.toString()
}

data class TrendLog(val date: String, val rating: Int)

/**
 * Find the trend of a user's rating
 */
fun findTrend(log: List<TrendLog>): List<TrendOut> {

    // Limit to 60 days by filtering out the dates that are too old
    val minDate = LocalDate.now().minusDays(60).isoDate()
    val now = LocalDate.now().isoDate()

    // O(n log n)
    val d = log.sortedBy { it.date }.toList()
    if (d.isEmpty()) return listOf()

    // Precompute the play counts for each date in O(n)
    val playCounts = d.groupingBy { it.date }.eachCount()

    // Find the max afterRating on each date
    val maxRating = d.groupingBy { it.date }.fold(0) { acc, e -> maxOf(acc, e.rating) }

    // Use the precomputed play counts
    val trend = d.distinctBy { it.date }
        .map { TrendOut(it.date, maxRating[it.date] ?: 0,
            playCounts[it.date] ?: 0) }
        .sortedBy { it.date }.mut

    // Fill in the missing dates (min date and current date)
    trend[0].let { if (it.date > minDate) trend.add(0, TrendOut(minDate, 0, 0)) }
    trend.last().let { if (it.date != now) trend.add(TrendOut(now, it.rating, 0)) }

    return trend
}

fun List<IGenericGamePlaylog>.acc() = if (isEmpty()) 0.0 else sumOf { it.achievement / 10000.0 } / size
