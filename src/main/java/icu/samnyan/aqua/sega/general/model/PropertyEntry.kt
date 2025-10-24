package icu.samnyan.aqua.sega.general.model

import jakarta.persistence.*

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "ServerPropertyEntry")
@Table(name = "property")
class PropertyEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(unique = true)
    var propertyKey: String = ""

    @Column(columnDefinition = "TEXT")
    var propertyValue: String = ""

    constructor(propertyKey: String, propertyValue: String) {
        this.propertyKey = propertyKey
        this.propertyValue = propertyValue
    }

    constructor()
}
