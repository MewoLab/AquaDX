package icu.samnyan.aqua.sega.diva.dao

import icu.samnyan.aqua.sega.diva.dao.gamedata.*
import icu.samnyan.aqua.sega.diva.dao.userdata.*
import org.springframework.stereotype.Component

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