package aquadx.sega.general.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CardTimestampRepo : JpaRepository<CardTimestamp, Long> {
    fun findByCardIdAndGameId(cardId: Long, gameId: String): Optional<CardTimestamp>
}
