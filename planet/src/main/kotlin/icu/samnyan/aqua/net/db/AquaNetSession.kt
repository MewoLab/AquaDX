package icu.samnyan.aqua.net.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionTokenRepo : JpaRepository<SessionToken, String> {
    fun findByToken(token: String): SessionToken?
    fun findByAquaNetUserAuId(auId: Long): List<SessionToken>
}
