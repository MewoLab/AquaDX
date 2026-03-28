package icu.samnyan.aqua.sega.allnet

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable

/**
 * This is the old method of securing requests - a keychip whitelist,
 * it's kept here only for backwards compatibility.
 */
@Entity
@Table(name = "allnet_keychips")
class Keychip(
    @Id
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val keychipId: String = "",
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}

@Repository("KeyChipRepository")
interface KeyChipRepo : JpaRepository<Keychip?, Long?> {
    fun existsByKeychipId(keychipId: String?): Boolean
}