package aquadx.sega.general.dao

import aquadx.sega.general.model.PropertyEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Repository
interface PropertyEntryRepository : JpaRepository<PropertyEntry, Long> {
    fun findByPropertyKey(key: String): PropertyEntry?
}
