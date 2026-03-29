package icu.samnyan.aqua.sega.allnet

import icu.samnyan.aqua.net.db.AquaNetUser
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "allnet_keychips")
class Keychip(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true, length = 32)
    var keychipId: String = "",

    @ManyToOne
    @JoinColumn(name = "auId")
    var user: AquaNetUser? = null
) : Serializable
