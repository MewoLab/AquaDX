package icu.samnyan.aqua.sega.general.model

class UserRecentRating(
    val musicId: Int = 0,
    val difficultId: Int = 0,
    val romVersionCode: String = "",
    val score: Int = 0
) {
    override fun toString() = "$musicId:$difficultId:$score"
}