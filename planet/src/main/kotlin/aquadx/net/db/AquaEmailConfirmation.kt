package aquadx.net.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailConfirmationRepo : JpaRepository<EmailConfirmation, Long> {
    fun findByToken(token: String): EmailConfirmation?
    fun findByAquaNetUserAuId(auId: Long): List<EmailConfirmation>
}
