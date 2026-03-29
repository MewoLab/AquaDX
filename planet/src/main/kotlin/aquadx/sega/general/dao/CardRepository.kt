package aquadx.sega.general.dao

import aquadx.sega.general.model.Card
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Repository("SegaCardRepository")
interface CardRepository : JpaRepository<Card, Long> {
    fun findByExtId(extId: Long): Card?
    fun findByLuid(luid: String): Card?
}
