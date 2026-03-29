package icu.samnyan.aqua.net

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface SafetyRepo : JpaRepository<Safety, Long> {
    fun findBySafetyId(safetyId: String): Safety?
}

@Service
class AquaNetSafetyService(val safetyRepo: SafetyRepo) {
    fun isSafe(safetyId: String): Boolean {
        val safety = safetyRepo.findBySafetyId(safetyId)
        return safety == null || safety.status == 0
    }

    fun isSafeBatch(safetyIds: List<String>): List<Boolean> {
        return safetyIds.map { isSafe(it) }
    }
}
