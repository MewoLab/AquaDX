package icu.samnyan.aqua.sega.diva.model.gamedata;

import icu.samnyan.aqua.sega.diva.model.common.ContestLeague;
import icu.samnyan.aqua.sega.diva.model.common.ContestNormaType;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "DivaContest")
@Table(name = "diva_contest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean enable;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ContestLeague league;

    private int stars;

    private int minComplexity; // Only use when Pv difficulty list is not set.

    private int maxComplexity;

    private int stages;

    private String stageLimit;

    @Enumerated(EnumType.STRING)
    private ContestNormaType normaType;

    private int bronzeBorders = 0;

    private int sliverBorders = 0;

    private int goldBorders = 0;

    // Pv List format: "pv_id_start:pv_id_end,pv_id_start:pv_id_end,pv_id_start:pv_id_end" more than 20 group will be ignore, put in -1 for empty end
    private String pvList;

    // Pv difficulty list format: "pv_difficulty:min_complexity:max_complexity"
    private String pvDiffList;

    // ContestReward format:
    // Reward Type: (-1 None, 0 VP, 1 Skin, 2 Callsign, 3 Customize)
    // Format: "rewardType:reward:string1:string2" string1 and 2 should be urlencoded and must exist. use *** aka %2A%2A%2A as placeholder
    private String bronzeContestReward;

    private String sliverContestReward;

    private String goldContestReward;

    // ContestReward format: "rewardType:reward:string1:string2"
    private String contestEntryReward;

    public String getString() {
        List<Object> list = new LinkedList<>();
        list.add(this.id); // Contest ID
        list.add(DivaDateTimeUtil.format(this.startTime)); // Start time
        list.add(DivaDateTimeUtil.format(this.endTime)); // End time
        list.add(URIEncoder.encode(this.name)); // Contest name
        list.add(URIEncoder.encode(this.description)); // Contest description
        list.add(this.league.getValue()); // Contest league
        list.add(this.stars); // Contest starts
        list.add(this.stages); // Contest stage, 1~9
        list.add(this.stageLimit); // list_lump_num ( 0 will be all stage same. > 1 will became stage max defined chart? )
        list.add(this.normaType.getValue());
        list.add(this.bronzeBorders);
        list.add(this.sliverBorders);
        list.add(this.goldBorders);
        for (int i = 1; i <= 20; i++) {
            // format is "pv_range_start,pv_range_end,min_complexity,max_complexity,difficulty,unknown"
            if (StringUtils.isBlank(pvList) || !pvList.contains(":")) {
                list.add(-1);
                list.add(-1);
                if (i == 1) {
                    list.add(this.minComplexity);
                    list.add(this.maxComplexity);
                } else {
                    list.add(-2);
                    list.add(-2);
                }
                list.add(-1);
                list.add(-2);
                list.add("7fffffffffffffffffffffffffffffff");
            } else {
                String[] groups = pvList.split(",");
                if (groups.length < i) {
                    list.add(-1);
                    list.add(-1);
                    list.add(-2);
                    list.add(-2);
                    list.add(-1);
                    list.add(-2);
                    list.add("7fffffffffffffffffffffffffffffff");
                } else {
                    String[] ids = groups[i - 1].split(":");
                    list.add(ids[0]);
                    list.add(ids[1]);
                    if (StringUtils.isBlank(pvDiffList) || !pvDiffList.contains(":")) {
                        list.add(this.minComplexity);
                        list.add(this.maxComplexity);
                        list.add(-1);
                    } else {
                        String[] diffList = pvDiffList.split(",");
                        if (diffList.length < i) {
                            list.add(this.minComplexity);
                            list.add(this.maxComplexity);
                            list.add(-1);
                        } else {
                            String[] diff = diffList[i - 1].split(":");
                            list.add(diff[1]);
                            list.add(diff[2]);
                            list.add(diff[0]);
                        }
                    }
                    list.add(-2);
                    list.add("7fffffffffffffffffffffffffffffff");
                }
            }
        }
        return list.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}
