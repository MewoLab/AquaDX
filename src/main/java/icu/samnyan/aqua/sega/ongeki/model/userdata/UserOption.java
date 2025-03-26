package icu.samnyan.aqua.sega.ongeki.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "OngekiUserOption")
@Table(name = "ongeki_user_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    private int optionSet;

    private int speed;

    private int mirror;

    private int judgeTiming;

    private int judgeAdjustment;

    private int abort;

    private int stealthField;

    private int tapSound;

    private int volGuide;

    private int volAll;

    private int volTap;

    private int volCrTap;

    private int volHold;

    private int volSide;

    private int volFlick;

    private int volBell;

    private int volEnemy;

    private int volSkill;

    private int volDamage;

    private int colorField;

    private int colorLaneBright;

    private int colorWallBright;

    private int colorLane;

    private int colorSide;

    private int effectDamage;

    private int effectPos;

    private int effectAttack;

    private int judgeDisp;

    private int judgePos;

    private int judgeBreak;

    private int judgeHit;

    private int platinumBreakDisp;

    private int judgeCriticalBreak;

    private int matching;

    private int dispPlayerLv;

    private int dispRating;

    private int dispBP;

    private int headphone;

    public UserOption(UserData userData) {
        this.user = userData;
    }
}
