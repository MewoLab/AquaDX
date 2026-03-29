package icu.samnyan.aqua.net.db

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "aqua_net_email_confirmation")
class EmailConfirmation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "auId")
    var aquaNetUser: AquaNetUser = AquaNetUser(),

    @Column(nullable = false, unique = true)
    var token: String = "",

    @Column(nullable = false)
    var createdAt: Instant = Instant.now()
) : Serializable
