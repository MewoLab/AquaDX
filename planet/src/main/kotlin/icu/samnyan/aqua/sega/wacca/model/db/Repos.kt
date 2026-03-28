package icu.samnyan.aqua.sega.wacca.model.db

import icu.samnyan.aqua.net.games.GenericPlaylogRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.GenericUserMusicRepo
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Component

interface WcUserRepo : JpaRepository<WaccaUser, Long>, GenericUserDataRepo<WaccaUser> {
    fun findByCardExtId(extId: Long): WaccaUser?
}

@NoRepositoryBean
interface IWaccaUserLinked<T> : JpaRepository<T, Long> {
    fun findByUser(user: WaccaUser): List<T>
    fun findByUserCardExtId(userId: Long): List<T>
    @Transactional
    fun deleteByUser(user: WaccaUser)
}

interface WcUserOptionRepo : IWaccaUserLinked<WcUserOption>
interface WcUserBingoRepo : IWaccaUserLinked<WcUserBingo> {
    fun findByUserAndPageNumber(user: WaccaUser, pageNumber: Int): WcUserBingo?
}
interface WcUserFriendRepo : IWaccaUserLinked<WcUserFriend>
interface WcUserGateRepo : IWaccaUserLinked<WcUserGate> {
    fun findByUserAndGateId(user: WaccaUser, gateId: Int): WcUserGate?
}
interface WcUserItemRepo : IWaccaUserLinked<WcUserItem> {
    fun findByUserAndType(user: WaccaUser, type: Int): List<WcUserItem>
    fun findByUserAndItemIdAndType(user: WaccaUser, itemId: Int, type: Int): WcUserItem?
}
interface WcUserBestScoreRepo : IWaccaUserLinked<WcUserScore>, GenericUserMusicRepo<WcUserScore> {
    fun findByUserAndMusicIdAndLevel(user: WaccaUser, songId: Int, level: Int): WcUserScore?
    @Query("SELECT SUM(achievement) FROM WcUserScore WHERE user = :user")
    fun sumScoreByUser(user: WaccaUser): Long
    @Query("SELECT s FROM WcUserScore s WHERE user = :user ORDER BY rating DESC LIMIT 50")
    fun findTop50(user: WaccaUser): List<WcUserScore>
}
interface WcUserPlayLogRepo : IWaccaUserLinked<WcUserPlayLog>, GenericPlaylogRepo<WcUserPlayLog>
interface WcUserStageUpRepo : IWaccaUserLinked<WcUserStageUp>

@Component
class WaccaRepos(
    val user: WcUserRepo,
    val option: WcUserOptionRepo,
    val bingo: WcUserBingoRepo,
    val friend: WcUserFriendRepo,
    val gate: WcUserGateRepo,
    val item: WcUserItemRepo,
    val bestScore: WcUserBestScoreRepo,
    val playLog: WcUserPlayLogRepo,
    val stageUp: WcUserStageUpRepo
)