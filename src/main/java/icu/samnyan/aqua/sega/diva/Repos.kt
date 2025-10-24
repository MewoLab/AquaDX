package icu.samnyan.aqua.sega.diva

import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.gamedata.*
import icu.samnyan.aqua.sega.diva.model.userdata.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    val ngWords: NgWordsRepository,
    val pvEntry: PvEntryRepository
)

@Component
class DivaRepos(
    val g: DivaGameRepos,
    val gameSession: GameSessionRepository,
    val playLog: PlayLogRepository,
    val contest: PlayerContestRepository,
    val customize: PlayerCustomizeRepository,
    val inventory: PlayerInventoryRepository,
    val module: PlayerModuleRepository,
    val profile: PlayerProfileRepository,
    val pvCustomize: PlayerPvCustomizeRepository,
    val pvRecord: PlayerPvRecordRepository,
    val screenShot: PlayerScreenShotRepository
)

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
interface NgWordsRepository : JpaRepository<NgWords, Int>

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

    fun findByPdId_PdId(pdId: Long, page: Pageable): Page<PlayerCustomize>

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

    fun findByPdId_PdIdAndPvId(pdId: Long, pvId: Int): Optional<PlayerPvCustomize>
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

    fun findByIdAndPdId_PdId(id: Long, pdId: Long): Optional<PlayerPvRecord>

    fun findByPdIdAndEdition(profile: PlayerProfile, edition: Edition): MutableList<PlayerPvRecord>

    fun findTop3ByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(
        pvId: Int,
        edition: Edition,
        difficulty: Difficulty
    ): MutableList<PlayerPvRecord>

    fun findByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(
        pvId: Int,
        edition: Edition,
        difficulty: Difficulty,
        page: Pageable
    ): Page<PlayerPvRecord>

    fun findByPdId_PdIdOrderByPvId(pdId: Long, page: Pageable): Page<PlayerPvRecord>

    fun findByPdId_PdIdAndPvId(pdId: Long, pvId: Int): MutableList<PlayerPvRecord>
}

interface PlayerScreenShotRepository : JpaRepository<PlayerScreenShot, Long> {
    fun findByPdId_PdId(pdId: Long): MutableList<PlayerScreenShot>
    fun findByFileName(fileName: String): Optional<PlayerScreenShot>
}

@Repository
interface PlayLogRepository : JpaRepository<PlayLog, Long> {
    fun findByPdId_PdIdOrderByDateTimeDesc(pdId: Long, page: Pageable): Page<PlayLog>
}

@Repository
interface PlayerModuleRepository : JpaRepository<PlayerModule, Long> {
    fun findByPdId(profile: PlayerProfile): MutableList<PlayerModule>

    fun findByPdId_PdId(pdId: Long, pageable: Pageable): Page<PlayerModule>
}