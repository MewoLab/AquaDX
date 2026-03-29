package icu.samnyan.aqua.net.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResetPasswordRepo : JpaRepository<ResetPassword, Long> {
    fun findByToken(token: String): ResetPassword?
    fun findByAquaNetUserAuId(auId: Long): List<ResetPassword>
}
