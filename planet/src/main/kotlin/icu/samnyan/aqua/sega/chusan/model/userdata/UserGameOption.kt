package icu.samnyan.aqua.sega.chusan.model.userdata
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity(name = "ChusanUserGameOption")
@Table(name = "chusan_user_game_option")
class UserGameOption : Chu3UserEntity() {
    var bgInfo = 0
    var fieldColor = 0
    var guideSound = 0
    var soundEffect = 0
    var guideLine = 0
    var speed = 0
    var optionSet = 0
    var matching = 0
    var judgePos = 0
    var rating = 0
    var judgeCritical = 0
    var judgeJustice = 0
    var judgeAttack = 0
    var headphone = 0
    var playerLevel = 0
    var successTap = 0
    var successExTap = 0
    var successSlideHold = 0
    var successAir = 0
    var successFlick = 0
    var successSkill = 0
    var successTapTimbre = 0
    var privacy = 0
    var mirrorFumen = 0
    var selectMusicFilterLv = 0
    var sortMusicFilterLv = 0
    var sortMusicGenre = 0
    var categoryDetail = 0
    var judgeTimingOffset = 0
    var playTimingOffset = 0
    var fieldWallPosition = 0
    var resultVoiceShort = 0
    var notesThickness = 0
    var judgeAppendSe = 0
    var trackSkip = 0
    var hardJudge = 0
    var speed_120 = 0
    var fieldWallPosition_120 = 0
    var playTimingOffset_120 = 0
    var judgeTimingOffset_120 = 0
    var ext1 = 0
    var ext2 = 0
    var ext3 = 0
    var ext4 = 0
    var ext5 = 0
    var ext6 = 0
    var ext7 = 0
    var ext8 = 0
    var ext9 = 0
    var ext10 = 0
}
