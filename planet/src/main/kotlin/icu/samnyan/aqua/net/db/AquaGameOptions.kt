package icu.samnyan.aqua.net.db

import com.fasterxml.jackson.annotation.JsonIgnore
import ext.SettingField
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class AquaGameOptions(
    @Id @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @SettingField("mai2") @Column(name = "mai2_unlock_music")
    var mai2UnlockMusic: Boolean = false,
    @SettingField("mai2") @Column(name = "mai2_unlock_chara")
    var mai2UnlockChara: Boolean = false,
    @SettingField("mai2") @Column(name = "mai2_unlock_chara_max_level")
    var mai2UnlockCharaMaxLevel: Boolean = false,
    @SettingField("mai2") @Column(name = "mai2_unlock_partners")
    var mai2UnlockPartners: Boolean = false,
    @SettingField("mai2") @Column(name = "mai2_unlock_collectables")
    var mai2UnlockCollectables: Boolean = false,
    @SettingField("mai2") @Column(name = "mai2_unlock_tickets")
    var mai2UnlockTickets: Boolean = false,

    @SettingField("wacca")
    var waccaUnlockMusic: Boolean = false,
    @SettingField("wacca")
    var waccaUnlockPlates: Boolean = false,
    @SettingField("wacca")
    var waccaUnlockCollectables: Boolean = false,
    @SettingField("wacca")
    var waccaUnlockTickets: Boolean = false,
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

    @SettingField("chu3-linked-verse")
    var chusanLvUnlockAll: Boolean = false,
    @SettingField("chu3-linked-verse")
    var chusanLvDifficulty: Int = 1,
    
    @SettingField("chu3-matching-chat")
    var chusanSymbolChat1: Int? = null,
    @SettingField("chu3-matching-chat")
    var chusanSymbolChat2: Int? = null,
    @SettingField("chu3-matching-chat")
    var chusanSymbolChat3: Int? = null,
    @SettingField("chu3-matching-chat")
    var chusanSymbolChat4: Int? = null,

    @SettingField("mai2")
    var enableMusicRank: Boolean = true,

    @SettingField("ongeki")
    var ongekiInfiniteKaika: Boolean = false,

    @SettingField("profile")
    var countryOverride: String = "",
)

interface AquaGameOptionsRepo : JpaRepository<AquaGameOptions, Long>
