package icu.samnyan.aqua.sega.diva

import ext.invoke
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.db.gamedata.*
import icu.samnyan.aqua.sega.diva.model.db.userdata.*
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*

@Component
class DivaGameRepos(
    val contest: ContestRepository,
    val customize: DivaCustomizeRepository,
    val module: DivaModuleRepository,
    val pv: DivaPvRepository,
    val festa: FestaRepository,
    val pvEntry: PvEntryRepository
)

@Component
class DivaRepos(
    val g: DivaGameRepos,
    val s: DivaServices,
    val gameSession: GameSessionRepository,
    val playLog: PlayLogRepository,
    val contest: PlayerContestRepository,
    val customize: PlayerCustomizeRepository,
    val inventory: PlayerInventoryRepository,
    val module: PlayerModuleRepository,
    val profile: PlayerProfileRepository,
    val pvCustomize: PlayerPvCustomizeRepository,
    val pvRecord: PlayerPvRecordRepository,
    val screenShot: PlayerScreenShotRepository,
) {
    fun profile(id: Long) = profile.findByPdId(id)() ?: throw ProfileNotFoundException()
    fun session(id: Long) = profile(id).let { it to (gameSession.findByPdId(it)() ?: throw SessionNotFoundException()) }
}

@Repository
interface ContestRepository : JpaRepository<Contest, Int> {
    fun findTop8ByEnable(enable: Boolean): MutableList<Contest>
}

@Repository
interface DivaCustomizeRepository : JpaRepository<DivaCustomize, Int>

@Repository
interface DivaModuleRepository : JpaRepository<DivaModule, Int>

interface DivaPvRepository : JpaRepository<Pv, Int>

@Repository
interface FestaRepository : JpaRepository<Festa, Int> {
    fun findTop2ByEnableOrderByCreateDateDesc(enable: Boolean): MutableList<Festa>
}

@Repository
interface PvEntryRepository : JpaRepository<PvEntry, Int> {
    fun findByDifficulty(difficulty: Difficulty): MutableList<PvEntry>
}

interface PlayerContestRepository : JpaRepository<PlayerContest, Long> {
    fun findByPdIdAndContestId(pdId: PlayerProfile, contestId: Int): Optional<PlayerContest>

    fun findTop4ByPdIdOrderByLastUpdateTimeDesc(pdId: PlayerProfile): MutableList<PlayerContest>
}

@Repository
interface GameSessionRepository : JpaRepository<GameSession, Long> {
    fun findByPdId(profile: PlayerProfile): Optional<GameSession>
}

@Repository
interface PlayerCustomizeRepository : JpaRepository<PlayerCustomize, Long> {
    fun findByPdId(profile: PlayerProfile): MutableList<PlayerCustomize>

    fun findByPdIdAndCustomizeId(currentProfile: PlayerProfile, parseInt: Int): Optional<PlayerCustomize>
}

@Repository
interface PlayerInventoryRepository : JpaRepository<PlayerInventory, Long> {
    fun findByPdIdAndTypeAndValue(profile: PlayerProfile, type: String, value: String): Optional<PlayerInventory>
}

@Repository
interface PlayerProfileRepository : JpaRepository<PlayerProfile, Long> {
    fun findByPdId(pdId: Long): Optional<PlayerProfile>
}

@Repository
interface PlayerPvCustomizeRepository : JpaRepository<PlayerPvCustomize, Long> {
    fun findByPdIdAndPvId(profile: PlayerProfile, pvId: Int): Optional<PlayerPvCustomize>
}

@Repository
interface PlayerPvRecordRepository : JpaRepository<PlayerPvRecord, Long> {
    fun findByPdIdAndPvIdAndEditionAndDifficulty(
        profile: PlayerProfile,
        pvId: Int,
        edition: Edition,
        difficulty: Difficulty
    ): Optional<PlayerPvRecord>

    fun findByPdId_PdIdAndPvIdAndEditionAndDifficulty(
        pdId: Long,
        pvId: Int,
        edition: Edition,
        difficulty: Difficulty
    ): Optional<PlayerPvRecord>

    @Query(
        ("SELECT COUNT(t1.id) as ranking from DivaPlayerPvRecord as t1 " +
            "where t1.maxScore >= (" +
            "SELECT maxScore from DivaPlayerPvRecord where pvId = :pvId and pdId = :pdId and edition = :edition and difficulty = :difficulty" +
            ") and t1.pvId = :pvId and t1.edition = :edition and t1.difficulty = :difficulty")
    )
    fun rankByPvIdAndPdIdAndEditionAndDifficulty(
        @Param("pvId") pvId: Int,
        @Param("pdId") pdId: PlayerProfile,
        @Param("edition") edition: Edition,
        @Param("difficulty") difficulty: Difficulty
    ): Int

    fun findByPdId(profile: PlayerProfile): MutableList<PlayerPvRecord>

    fun findByPdIdAndEdition(profile: PlayerProfile, edition: Edition): MutableList<PlayerPvRecord>

    fun findTop3ByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(
        pvId: Int,
        edition: Edition,
        difficulty: Difficulty
    ): MutableList<PlayerPvRecord>
}

interface PlayerScreenShotRepository : JpaRepository<PlayerScreenShot, Long>

@Repository
interface PlayLogRepository : JpaRepository<PlayLog, Long>

@Repository
interface PlayerModuleRepository : JpaRepository<PlayerModule, Long> {
    fun findByPdId(profile: PlayerProfile): MutableList<PlayerModule>
}