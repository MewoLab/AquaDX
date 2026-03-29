package icu.samnyan.aqua.sega.allnet

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KeyChipRepo : JpaRepository<Keychip, Long> {
    fun findByKeychipId(keychipId: String): Keychip?
    fun existsByKeychipId(keychipId: String): Boolean
}
