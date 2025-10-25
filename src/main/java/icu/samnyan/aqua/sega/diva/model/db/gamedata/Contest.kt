package icu.samnyan.aqua.sega.diva.model.db.gamedata

import ext.csv
import icu.samnyan.aqua.sega.diva.model.common.ContestLeague
import icu.samnyan.aqua.sega.diva.model.common.ContestNormaType
import icu.samnyan.aqua.sega.diva.util.DivaTime
import icu.samnyan.aqua.sega.diva.util.URIEncoder
import jakarta.persistence.*
import org.apache.commons.lang3.StringUtils
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaContest")
@Table(name = "diva_contest")
class Contest : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0
    var enable = false
    var startTime: LocalDateTime = LocalDateTime.now()
    var endTime: LocalDateTime = LocalDateTime.now()
    var name: String = ""
    var description: String = ""

    @Enumerated(EnumType.STRING)
    var league: ContestLeague = ContestLeague.BEGINNER
    var stars = 0
    var minComplexity = 0 // Only use when Pv difficulty list is not set.
    var maxComplexity = 0
    var stages = 0
    var stageLimit: String = ""

    @Enumerated(EnumType.STRING)
    var normaType: ContestNormaType = ContestNormaType.SCORE
    var bronzeBorders = 0
    var sliverBorders = 0
    var goldBorders = 0

    // Pv List format: "pv_id_start:pv_id_end,pv_id_start:pv_id_end,pv_id_start:pv_id_end" more than 20 group will be ignore, put in -1 for empty end
    var pvList: String = ""

    // Pv difficulty list format: "pv_difficulty:min_complexity:max_complexity"
    var pvDiffList: String = ""

    // ContestReward format:
    // Reward Type: (-1 None, 0 VP, 1 Skin, 2 Callsign, 3 Customize)
    // Format: "rewardType:reward:string1:string2" string1 and 2 should be urlencoded and must exist. use *** aka %2A%2A%2A as placeholder
    var bronzeContestReward: String = ""
    var sliverContestReward: String = ""
    var goldContestReward: String = ""

    // ContestReward format: "rewardType:reward:string1:string2"
    var contestEntryReward: String = ""

    constructor()

    val string: String
        get() {
            val list = mutableListOf(
                this.id, // Contest ID
                DivaTime.format(this.startTime), // Start time
                DivaTime.format(this.endTime), // End time
                URIEncoder.encode(this.name), // Contest name
                URIEncoder.encode(this.description), // Contest description
                this.league.value, // Contest league
                this.stars, // Contest starts
                this.stages, // Contest stage, 1~9
                this.stageLimit, // list_lump_num ( 0 will be all stage same. > 1 will became stage max defined chart )
                this.normaType.value,
                this.bronzeBorders,
                this.sliverBorders,
                this.goldBorders
            )
            for (i in 1..20) {
                // format is "pv_range_start,pv_range_end,min_complexity,max_complexity,difficulty,unknown"
                if (pvList.isBlank() || !pvList.contains(":")) {
                    list += listOf(-1, -1)
                    if (i == 1) {
                        list.add(this.minComplexity)
                        list.add(this.maxComplexity)
                    } else {
                        list.add(-2)
                        list.add(-2)
                    }
                    list += listOf(-1, -2, "7fffffffffffffffffffffffffffffff")
                } else {
                    val groups = pvList.split(',').dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (groups.size < i) {
                        list += listOf(-1, -1, -2, -2, -1, -2, "7fffffffffffffffffffffffffffffff")
                    } else {
                        val ids = groups[i - 1].split(':').dropLastWhile { it.isEmpty() }.toTypedArray()
                        list.add(ids[0])
                        list.add(ids[1])
                        if (StringUtils.isBlank(pvDiffList) || !pvDiffList.contains(":")) {
                            list.add(this.minComplexity)
                            list.add(this.maxComplexity)
                            list.add(-1)
                        } else {
                            val diffList = pvDiffList.split(',').dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (diffList.size < i) {
                                list.add(this.minComplexity)
                                list.add(this.maxComplexity)
                                list.add(-1)
                            } else {
                                val diff = diffList[i - 1].split(':').dropLastWhile { it.isEmpty() }.toTypedArray()
                                list.add(diff[1])
                                list.add(diff[2])
                                list.add(diff[0])
                            }
                        }
                        list.add(-2)
                        list.add("7fffffffffffffffffffffffffffffff")
                    }
                }
            }
            return list.csv
        }
}
