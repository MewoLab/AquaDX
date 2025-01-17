package icu.samnyan.aqua.net.db

import com.fasterxml.jackson.annotation.JsonIgnore
import ext.SettingField
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class AquaGameOptions(
    @Id @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @SettingField("general")
    var unlockMusic: Boolean = false,

    @SettingField("general")
    var unlockChara: Boolean = false,

    @SettingField("general")
    var unlockCollectables: Boolean = false,

    @SettingField("general")
    var unlockTickets: Boolean = false,

    @SettingField("wacca")
    var waccaInfiniteWp: Boolean = false,

    @SettingField("wacca")
    var waccaAlwaysVip: Boolean = false,

    @SettingField("chu3")
    var chusanTeamName: String = "",

    @SettingField("chu3")
    var chusanInfinitePenguins: Boolean = false,

    @SettingField("chu3-matching")
    var chusanMatchingServer: String = "",

    @SettingField("chu3-matching")
    var chusanMatchingReflector: String = "",

    @SettingField("mai2")
    var enableMusicRank: Boolean = true,
)

interface AquaGameOptionsRepo : JpaRepository<AquaGameOptions, Long>
