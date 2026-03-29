package icu.samnyan.aqua.sega.diva.model.db.gamedata

import ext.csv
import icu.samnyan.aqua.sega.diva.model.common.ContestLeague
import icu.samnyan.aqua.sega.diva.model.common.ContestNormaType
import icu.samnyan.aqua.sega.diva.util.DivaTime
import icu.samnyan.aqua.sega.diva.util.URIEncoder
import jakarta.persistence.*
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
    var minComplexity = 0
    var maxComplexity = 0
    var stages = 0
    var stageLimit: String = ""

    @Enumerated(EnumType.STRING)
    var normaType: ContestNormaType = ContestNormaType.SCORE
    var bronzeBorders = 0
    var sliverBorders = 0
    var goldBorders = 0

    var pvList: String = ""
    var pvDiffList: String = ""

    var bronzeContestReward: String = ""
    var sliverContestReward: String = ""
    var goldContestReward: String = ""
    var contestEntryReward: String = ""

    constructor()

    val string: String
        get() {
            val list = mutableListOf<Any>(
                this.id,
                DivaTime.format(this.startTime),
                DivaTime.format(this.endTime),
                URIEncoder.encode(this.name),
                URIEncoder.encode(this.description),
                this.league.value,
                this.stars,
                this.stages,
                this.stageLimit,
                this.normaType.value,
                this.bronzeBorders,
                this.sliverBorders,
                this.goldBorders
            )
            for (i in 1..20) {
                if (pvList.isBlank() || !pvList.contains(":")) {
                    list.addAll(listOf(-1, -1))
                    if (i == 1) {
                        list.add(this.minComplexity)
                        list.add(this.maxComplexity)
                    } else {
                        list.add(-2)
                        list.add(-2)
                    }
                    list.addAll(listOf(-1, -2, "7fffffffffffffffffffffffffffffff"))
                } else {
                    val groups = pvList.split(',').dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (groups.size < i) {
                        list.addAll(listOf(-1, -1, -2, -2, -1, -2, "7fffffffffffffffffffffffffffffff"))
                    } else {
                        val ids = groups[i - 1].split(':').dropLastWhile { it.isEmpty() }.toTypedArray()
                        list.add(ids[0])
                        list.add(ids[1])
                        if (pvDiffList.isBlank() || !pvDiffList.contains(":")) {
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
