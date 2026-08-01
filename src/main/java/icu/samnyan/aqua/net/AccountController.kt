package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.net.db.*
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.allnet.KeychipSessionRepo
import icu.samnyan.aqua.sega.allnet.UserKeychipRepo
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerProfile
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.ongeki.OngekiUserRepos
import icu.samnyan.aqua.sega.wacca.model.db.WaccaRepos
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.name

data class AccountGameDataExport(
    val formatVersion: Int = 1,
    val exportedAt: Instant = Instant.now(),
    val username: String,
    val gameOptions: AquaGameOptions?,
    val games: Map<String, Any>,
)

data class AccountFileCleanup(
    val profilePicture: String?,
    val gameUserId: Long,
    val divaScreenshots: List<String>,
)

@RestController
@API("/api/v2/user")
class AccountController(
    val jwt: JWT,
    val accountData: AccountDataService,
) {
    @API("/export-data")
    @Doc("Export all game data associated with the current user's account.", "All account game data")
    fun exportData(@RP token: Str) = jwt.auth(token) { accountData.export(it) }

    @API("/delete-account")
    @Doc("Permanently delete the current user's account and all associated game data.", "Success message")
    fun deleteAccount(@RP token: Str) = jwt.auth(token) { user ->
        val cleanup = accountData.deleteDatabaseData(user.auId)
        accountData.deleteUploadedFiles(cleanup)
        SUCCESS
    }
}

@Service
class AccountDataService(
    val userRepo: AquaNetUserRepo,
    val cardRepo: CardRepository,
    val gameOptionsRepo: AquaGameOptionsRepo,
    val sessionRepo: SessionTokenRepo,
    val confirmationRepo: EmailConfirmationRepo,
    val resetPasswordRepo: ResetPasswordRepo,
    val userKeychipRepo: UserKeychipRepo,
    val keychipSessionRepo: KeychipSessionRepo,
    val mai2: Mai2Repos,
    val chu3: Chu3Repos,
    val ongeki: OngekiUserRepos,
    val wacca: WaccaRepos,
    val diva: DivaRepos,
    val paths: PathProps,
) {
    companion object {
        val log = logger()
    }

    fun export(user: AquaNetUser): AccountGameDataExport {
        val card = user.ghostCard
        val games = linkedMapOf<String, Any>()

        exportMai2(card)?.let { games["mai2"] = it }
        exportChu3(card)?.let { games["chu3"] = it }
        exportOngeki(card)?.let { games["ongeki"] = it }
        exportWacca(card)?.let { games["wacca"] = it }
        exportDiva(card)?.let { games["diva"] = it }

        log.info("Exported all account game data for user ${user.auId}")
        return AccountGameDataExport(username = user.username, gameOptions = user.gameOptions, games = games)
    }

    private fun exportMai2(card: Card): Map<String, Any>? {
        val user = mai2.userData.findByCard(card) ?: return null
        return linkedMapOf(
            "gameId" to "SDEZ",
            "userData" to user,
            "mapEncountNpcList" to mai2.mapEncountNpc.findByUser(user),
            "userActList" to mai2.userAct.findByUser(user),
            "userCardList" to mai2.userCard.findByUser(user),
            "userCharacterList" to mai2.userCharacter.findByUser(user),
            "userChargeList" to mai2.userCharge.findByUser(user),
            "userCourseList" to mai2.userCourse.findByUser(user),
            "userExtendList" to mai2.userExtend.findByUser(user),
            "userFavoriteList" to mai2.userFavorite.findByUser(user),
            "userFriendSeasonRankingList" to mai2.userFriendSeasonRanking.findByUser(user),
            "userGeneralDataList" to mai2.userGeneralData.findByUser(user),
            "userItemList" to mai2.userItem.findByUser(user),
            "userLoginBonusList" to mai2.userLoginBonus.findByUser(user),
            "userMapList" to mai2.userMap.findByUser(user),
            "userMusicDetailList" to mai2.userMusicDetail.findByUser(user),
            "userOptionList" to mai2.userOption.findByUser(user),
            "userPlaylogList" to mai2.userPlaylog.findByUser(user),
            "userPrintDetailList" to mai2.userPrintDetail.findByUser(user),
            "userUdemaeList" to mai2.userUdemae.findByUser(user),
            "userKaleidxScopeList" to mai2.userKaleidx.findByUser(user),
            "userIntimateList" to mai2.userIntimate.findByUser(user),
            "userRegionsList" to mai2.userRegions.findByUser(user),
        )
    }

    private fun exportChu3(card: Card): Map<String, Any>? {
        val user = chu3.userData.findByCard(card) ?: return null
        return linkedMapOf(
            "gameId" to "SDHD",
            "userData" to user,
            "userActivityList" to chu3.userActivity.findByUser(user),
            "userCardPrintStateList" to chu3.userCardPrintState.findByUser(user),
            "userCharacterList" to chu3.userCharacter.findByUser(user),
            "userChargeList" to chu3.userCharge.findByUser(user),
            "userCourseList" to chu3.userCourse.findByUser(user),
            "userDuelList" to chu3.userDuel.findByUser(user),
            "userGachaList" to chu3.userGacha.findByUser(user),
            "userGameOptionList" to chu3.userGameOption.findByUser(user),
            "userGeneralDataList" to chu3.userGeneralData.findByUser(user),
            "userItemList" to chu3.userItem.findByUser(user),
            "userMapList" to chu3.userMap.findByUser(user),
            "userMusicDetailList" to chu3.userMusicDetail.findByUser(user),
            "userPlaylogList" to chu3.userPlaylog.findByUser(user),
            "userRegionsList" to chu3.userRegions.findByUser(user),
            "userCMissionList" to chu3.userCMission.findByUser(user),
            "userCMissionProgressList" to chu3.userCMissionProgress.findByUser(user),
            "netBattleLogList" to chu3.netBattleLog.findByUser(user),
            "userMiscList" to chu3.userMisc.findByUser(user),
            "userChallengeList" to chu3.userChallenge.findByUser(user),
            "userLinkedVerseList" to chu3.userLinkedVerse.findByUser(user),
            "userMateList" to chu3.userMate.findByUser(user),
            "userVoteList" to chu3.userVote.findByUser(user),
            "userLoginBonusList" to chu3.userLoginBonus.findByUser(card.extId.toInt()),
        )
    }

    private fun exportOngeki(card: Card): Map<String, Any>? {
        val user = ongeki.data.findByCard(card) ?: return null
        return linkedMapOf(
            "gameId" to "SDDT",
            "userData" to user,
            "userActivityList" to ongeki.activity.findByUser(user),
            "userBossList" to ongeki.boss.findByUser(user),
            "userCardList" to ongeki.card.findByUser(user),
            "userChapterList" to ongeki.chapter.findByUser(user),
            "userCharacterList" to ongeki.character.findByUser(user),
            "userDeckList" to ongeki.deck.findByUser(user),
            "userEventMusicList" to ongeki.eventMusic.findByUser(user),
            "userEventPointList" to ongeki.eventPoint.findByUser(user),
            "userGeneralDataList" to ongeki.generalData.findByUser(user),
            "userItemList" to ongeki.item.findByUser(user),
            "userKopList" to ongeki.kop.findByUser(user),
            "userLoginBonusList" to ongeki.loginBonus.findByUser(user),
            "userMemoryChapterList" to ongeki.memoryChapter.findByUser(user),
            "userMissionPointList" to ongeki.missionPoint.findByUser(user),
            "userMusicDetailList" to ongeki.musicDetail.findByUser(user),
            "userMusicItemList" to ongeki.musicItem.findByUser(user),
            "userOptionList" to ongeki.option.findByUser(user),
            "userPlaylogList" to ongeki.playlog.findByUser(user),
            "userRivalList" to ongeki.rival.findByUser(user),
            "userScenarioList" to ongeki.scenario.findByUser(user),
            "userStoryList" to ongeki.story.findByUser(user),
            "userTechCountList" to ongeki.techCount.findByUser(user),
            "userTechEventList" to ongeki.techEvent.findByUser(user),
            "userTradeItemList" to ongeki.tradeItem.findByUser(user),
            "userTrainingRoomList" to ongeki.trainingRoom.findByUser(user),
            "userEventMapList" to ongeki.eventMap.findByUser(user),
            "userSkinList" to ongeki.skin.findByUser(user),
            "userRegionsList" to ongeki.regions.findByUser(user),
            "userGachaList" to ongeki.gacha.findByUser(user),
        )
    }

    private fun exportWacca(card: Card): Map<String, Any>? {
        val user = wacca.user.findByCard(card) ?: return null
        return linkedMapOf(
            "gameId" to "SDFE",
            "userData" to user,
            "userOptionList" to wacca.option.findByUser(user),
            "userBingoList" to wacca.bingo.findByUser(user),
            "userFriendList" to wacca.friend.findByUser(user).map {
                mapOf("withUserId" to it.with.id, "isAccepted" to it.isAccepted)
            },
            "userGateList" to wacca.gate.findByUser(user),
            "userItemList" to wacca.item.findByUser(user),
            "userBestScoreList" to wacca.bestScore.findByUser(user),
            "userPlaylogList" to wacca.playLog.findByUser(user),
            "userStageUpList" to wacca.stageUp.findByUser(user),
        )
    }

    private fun exportDiva(card: Card): Map<String, Any>? {
        val profile = diva.profile.findByPdId(card.extId).orElse(null) ?: return null
        return linkedMapOf(
            "gameId" to "SBZV",
            "userData" to profile,
            "gameSession" to diva.gameSession.findByPdId(profile).orElse(null),
            "playLogList" to diva.playLog.findByPdId(profile),
            "playerContestList" to diva.contest.findByPdId(profile),
            "playerCustomizeList" to diva.customize.findByPdId(profile),
            "playerInventoryList" to diva.inventory.findByPdId(profile),
            "playerModuleList" to diva.module.findByPdId(profile),
            "playerPvCustomizeList" to diva.pvCustomize.findByPdId(profile),
            "playerPvRecordList" to diva.pvRecord.findByPdId(profile),
            "playerScreenShotList" to diva.screenShot.findByPdId(profile),
        )
    }

    @Transactional
    fun deleteDatabaseData(auId: Long): AccountFileCleanup {
        val user = userRepo.findByAuId(auId) ?: (404 - "User not found")
        val ghostCard = user.ghostCard
        val divaScreenshots = deleteDiva(ghostCard)

        mai2.userData.findByCard(ghostCard)?.let { mai2.userData.delete(it) }
        chu3.userData.findByCard(ghostCard)?.let { chu3.userData.delete(it) }
        ongeki.data.findByCard(ghostCard)?.let { ongeki.data.delete(it) }
        wacca.user.findByCard(ghostCard)?.let { wacca.user.delete(it) }
        chu3.userLoginBonus.deleteAll(chu3.userLoginBonus.findByUser(ghostCard.extId.toInt()))

        sessionRepo.deleteAll(sessionRepo.findByAquaNetUserAuId(auId))
        confirmationRepo.deleteAll(confirmationRepo.findByAquaNetUserAuId(auId))
        resetPasswordRepo.deleteAll(resetPasswordRepo.findByAquaNetUserAuId(auId))
        userKeychipRepo.deleteAll(userKeychipRepo.findAllByUserAuId(auId))
        keychipSessionRepo.deleteAll(keychipSessionRepo.findAllByUserAuId(auId))

        val linkedCards = (cardRepo.findAllByAquaUserAuId(auId) + ghostCard).distinctBy { it.id }
        linkedCards.forEach { it.aquaUser = null }
        cardRepo.saveAll(linkedCards)
        cardRepo.flush()

        user.cards.clear()
        user.keychips.clear()
        user.keychipSessions.clear()
        val gameOptions = user.gameOptions
        user.gameOptions = null

        userRepo.delete(user)
        userRepo.flush()
        cardRepo.delete(ghostCard)
        cardRepo.flush()
        gameOptions?.let { gameOptionsRepo.delete(it) }

        log.info("Deleted account and game data for user $auId")
        return AccountFileCleanup(user.profilePicture, ghostCard.extId, divaScreenshots)
    }

    private fun deleteDiva(card: Card): List<String> {
        val profile: PlayerProfile = diva.profile.findByPdId(card.extId).orElse(null) ?: return emptyList()
        val screenshots = diva.screenShot.findByPdId(profile)

        diva.gameSession.findByPdId(profile).ifPresent { diva.gameSession.delete(it) }
        diva.playLog.deleteAll(diva.playLog.findByPdId(profile))
        diva.contest.deleteAll(diva.contest.findByPdId(profile))
        diva.customize.deleteAll(diva.customize.findByPdId(profile))
        diva.inventory.deleteAll(diva.inventory.findByPdId(profile))
        diva.module.deleteAll(diva.module.findByPdId(profile))
        diva.pvCustomize.deleteAll(diva.pvCustomize.findByPdId(profile))
        diva.pvRecord.deleteAll(diva.pvRecord.findByPdId(profile))
        diva.screenShot.deleteAll(screenshots)
        diva.profile.delete(profile)

        return screenshots.map { it.fileName }
    }

    fun deleteUploadedFiles(cleanup: AccountFileCleanup) {
        try {
            cleanup.profilePicture
                ?.takeIf { it.isNotBlank() && Path(it).name == it }
                ?.let { (Path(paths.aquaNetPortrait) / it).deleteIfExists() }

            deleteFilesStartingWith(Path(paths.mai2Portrait), "${cleanup.gameUserId}-")
            deleteFilesStartingWith(Path(paths.mai2Plays), "${cleanup.gameUserId}-")
            deleteFilesStartingWith(Path("data/tmp"), "${cleanup.gameUserId}-")
            cleanup.divaScreenshots
                .filter { Path(it).name == it }
                .forEach { (Path("data") / it).deleteIfExists() }
        } catch (e: Exception) {
            // Database deletion has already committed. Leave a useful trail for administrators
            // rather than turning a successful account deletion into a client-visible failure.
            log.error("Failed to delete one or more uploaded files for deleted user ${cleanup.gameUserId}", e)
        }
    }

    private fun deleteFilesStartingWith(directory: Path, prefix: String) {
        if (!directory.exists()) return
        Files.list(directory).use { files ->
            files.filter { it.fileName.toString().startsWith(prefix) }
                .forEach { it.deleteIfExists() }
        }
    }
}
