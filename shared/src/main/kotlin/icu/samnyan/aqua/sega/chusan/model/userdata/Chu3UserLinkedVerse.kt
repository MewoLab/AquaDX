package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime


@Entity(name = "ChusanUserLinkedVerse")
@Table(name = "chusan_user_linked_verse", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "linked_verse_id"])])
class Chu3UserLinkedVerse : Chu3UserEntity() {
    var linkedVerseId = 0
    var clearCourseId = 0
    var clearCourseLevel = 0
    var clearDate: LocalDateTime = LocalDateTime.now()
    var clearUserId1: Long = 0
    var clearUserId2: Long = 0
    var clearUserId3: Long = 0
    var clearUserName0 = ""
    var clearUserName1 = ""
    var clearUserName2 = ""
    var clearUserName3 = ""
    var isFirstClear = false
    var numClear = 0
    var statusOpen = 0
    var statusUnlock = 0
    var progress: String = ""
}
