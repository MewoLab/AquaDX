package icu.samnyan.aqua.sega.diva.model.db.gamedata

import icu.samnyan.aqua.sega.diva.model.Internalizable
import icu.samnyan.aqua.sega.diva.util.DivaTime
import icu.samnyan.aqua.sega.diva.util.URIEncoder
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaModule")
@Table(name = "diva_module")
class DivaModule : Serializable, Internalizable {
    @Id
    var id = 0
    var name: String = ""
    var price = 0
    var releaseDate: LocalDateTime = LocalDateTime.now()
    var endDate: LocalDateTime = LocalDateTime.now()
    var sortOrder = 0

    override fun toInternal() = "$id,0,${URIEncoder.encode(name)},$price,${DivaTime.format(releaseDate)},${DivaTime.format(endDate)},$sortOrder"
}
