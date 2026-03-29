package icu.samnyan.aqua.net

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "aqua_net_safety")
class Safety(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true, length = 64)
    var safetyId: String = "",

    @Column(nullable = false)
    var status: Int = 0
) : Serializable
