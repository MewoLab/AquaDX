package aquadx.net.games

import aquadx.sega.general.model.Card
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface GenericUserDataRepo<T : IUserData> : JpaRepository<T, Long> {
    fun findByCard(card: Card): T?
    fun findByCard_ExtId(extId: Long): T?

    @Query("select e from #{'#'}{#entityName} e where e.card.rankingBanned = false")
    fun findAllNonBanned(): List<T>
}

@NoRepositoryBean
interface GenericPlaylogRepo<T: IGenericGamePlaylog> : JpaRepository<T, Long> {
    fun findByUserCardExtId(extId: Long): List<T>
    fun findByUserCardExtId(extId: Long, page: Pageable): Page<T>
}

@NoRepositoryBean
interface GenericUserMusicRepo<T: IGenericUserMusic> : JpaRepository<T, Long> {
    fun findByUserCardExtId(extId: Long): List<T>
    fun findByUser_Card_ExtIdAndMusicIdIn(userId: Long, musicId: List<Int>): List<T>
}
