package icu.samnyan.aqua.net

import ext.JACKSON
import ext.logger
import ext.parse
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository
import icu.samnyan.aqua.sega.general.model.PropertyEntry
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service


@Service
class Migrations(
    val userRepo: AquaNetUserRepo,
    val cardRepo: CardRepository,
    val props: PropertyEntryRepository
) {
    companion object {
        val log = logger()
    }

    @PostConstruct
    fun migrate() {
        val db = props.findByPropertyKey("migrations") ?: PropertyEntry("migrations", "[]")
        val p = JACKSON.parse<ArrayList<String>>(db.propertyValue)
        val old = p.size

        if ("0. Signed ID" !in p) {
            migrateSignedID()
            p.add("0. Signed ID")
        }

        if (p.size > old) props.save(db.apply { propertyValue = JACKSON.writeValueAsString(p) })
    }

    /**
     * Migrate the ID to remove the signed bit
     */
    fun migrateSignedID() {
        // Loop through everyone's ID, remove the signed bit
        val cards = cardRepo.findAll()
        val max = Int.MAX_VALUE.toLong()
        cards.forEach { c ->
            if (c.extId > max) {
                var new = c.extId and max
                log.info("Removing signed bit: {${c.extId} -> $new} for ${c.luid}")
                while (cardRepo.findByExtId(new) != null) {
                    log.error("> Conflicting card found for ${c.luid}: $new")
                    new++
                }
                cardRepo.save(c.apply { extId = new; luid = new.toString() })
            }
        }
    }
}