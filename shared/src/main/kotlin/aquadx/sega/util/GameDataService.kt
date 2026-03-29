package icu.samnyan.aqua.sega.util

import com.fasterxml.jackson.core.type.TypeReference
import ext.logger
import ext.toJson
import icu.samnyan.aqua.sega.chusan.model.GameCharge as Chu3GameCharge
import icu.samnyan.aqua.sega.chusan.model.GameEvent as Chu3GameEvent
import icu.samnyan.aqua.sega.chusan.model.GameGacha as Chu3GameGacha
import icu.samnyan.aqua.sega.chusan.model.GameGachaCard as Chu3GameGachaCard
import icu.samnyan.aqua.sega.chusan.model.GameLinkedVerse as Chu3GameLinkedVerse
import icu.samnyan.aqua.sega.chusan.model.GameLoginBonus as Chu3GameLoginBonus
import icu.samnyan.aqua.sega.chusan.model.GameLoginBonusPreset as Chu3GameLoginBonusPreset
import icu.samnyan.aqua.sega.maimai2.model.Mai2GameCharge
import icu.samnyan.aqua.sega.maimai2.model.Mai2GameEvent
import icu.samnyan.aqua.sega.maimai2.model.Mai2GameSellingCard
import icu.samnyan.aqua.sega.ongeki.model.GameCard as OgkGameCard
import icu.samnyan.aqua.sega.ongeki.model.GameChara as OgkGameChara
import icu.samnyan.aqua.sega.ongeki.model.GameEvent as OgkGameEvent
import icu.samnyan.aqua.sega.ongeki.model.GameGacha as OgkGameGacha
import icu.samnyan.aqua.sega.ongeki.model.GameGachaCard as OgkGameGachaCard
import icu.samnyan.aqua.sega.ongeki.model.GameMusic as OgkGameMusic
import icu.samnyan.aqua.sega.ongeki.model.GamePoint as OgkGamePoint
import icu.samnyan.aqua.sega.ongeki.model.GamePresent as OgkGamePresent
import icu.samnyan.aqua.sega.ongeki.model.GameReward as OgkGameReward
import icu.samnyan.aqua.sega.ongeki.model.GameSkill as OgkGameSkill
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
class GameDataService() {
    companion object {
        val log = logger()
        val mapper = BasicMapper()

        inline fun <reified T> load(game: String, file: String): List<T> {
            val f = File("data/game/$game/$file")
            if (f.exists()) return mapper.read(f.readText(), object : TypeReference<List<T>>() {})

            // Try load from resources
            val resPath = "/data/game/$game/$file"
            val resStream = GameDataService::class.java.getResourceAsStream(resPath)
            if (resStream != null) {
                log.info("Loading game data from internal resources: $resPath")
                return mapper.read(resStream.bufferedReader().use { it.readText() }, object : TypeReference<List<T>>() {})
            }

            log.warn("Game data file $f or resource $resPath not found, using empty list")
            return emptyList()
        }
    }

    // maimai2
    lateinit var mai2Events: List<Mai2GameEvent>
    lateinit var mai2Charges: List<Mai2GameCharge>
    lateinit var mai2SellingCards: List<Mai2GameSellingCard>

    // chusan
    lateinit var chu3GameLinkedVerses: List<Chu3GameLinkedVerse>
    lateinit var chu3GameCharges: List<Chu3GameCharge>
    lateinit var chu3GameEvents: List<Chu3GameEvent>
    lateinit var chu3GameGachaCards: List<Chu3GameGachaCard>
    lateinit var chu3GameGachas: List<Chu3GameGacha>
    lateinit var chu3GameLoginBonusPresets: List<Chu3GameLoginBonusPreset>
    lateinit var chu3GameLoginBonuses: List<Chu3GameLoginBonus>

    // ongeki
    lateinit var ogkGameCards: List<OgkGameCard>
    lateinit var ogkGameCharas: List<OgkGameChara>
    lateinit var ogkGameEvents: List<OgkGameEvent>
    lateinit var ogkGameMusics: List<OgkGameMusic>
    lateinit var ogkGamePoints: List<OgkGamePoint>
    lateinit var ogkGamePresents: List<OgkGamePresent>
    lateinit var ogkGameRewards: List<OgkGameReward>
    lateinit var ogkGameSkills: List<OgkGameSkill>
    lateinit var ogkGameGachaCards: List<OgkGameGachaCard>
    lateinit var ogkGameGachas: List<OgkGameGacha>

    init {
        load()
    }

    fun load() {
        mai2Events = load("maimai2", "game_event.json")
        mai2Charges = load("maimai2", "game_charge.json")
        mai2SellingCards = load("maimai2", "game_selling_card.json")

        chu3GameLinkedVerses = load("chusan", "game_linked_verse.json")
        chu3GameCharges = load("chusan", "game_charge.json")
        chu3GameEvents = load("chusan", "game_event.json")
        chu3GameGachaCards = load("chusan", "game_gacha_card.json")
        chu3GameGachas = load("chusan", "game_gacha.json")
        chu3GameLoginBonusPresets = load("chusan", "game_login_bonus_preset.json")
        chu3GameLoginBonuses = load("chusan", "game_login_bonus.json")

        ogkGameCards = load("ongeki", "game_card.json")
        ogkGameCharas = load("ongeki", "game_chara.json")
        ogkGameEvents = load("ongeki", "game_event.json")
        ogkGameMusics = load("ongeki", "game_music.json")
        ogkGamePoints = load("ongeki", "game_point.json")
        ogkGamePresents = load("ongeki", "game_present.json")
        ogkGameRewards = load("ongeki", "game_reward.json")
        ogkGameSkills = load("ongeki", "game_skill.json")
        ogkGameGachaCards = load("ongeki", "game_gacha_card.json")
        ogkGameGachas = load("ongeki", "game_gacha.json")
    }
}

fun main(args: Array<String>) {
    GameDataService().let {
        it.load()
        println(it.mai2Events[0].toJson())
    }
}
