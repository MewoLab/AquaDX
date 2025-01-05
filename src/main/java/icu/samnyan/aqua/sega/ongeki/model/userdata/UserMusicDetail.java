package icu.samnyan.aqua.sega.ongeki.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import icu.samnyan.aqua.net.games.IGenericUserMusic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "OngekiUserMusicDetail")
@Table(name = "ongeki_user_music_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMusicDetail implements Serializable, IGenericUserMusic {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    private int musicId;

    private int level;

    private int playCount;

    private int techScoreMax;

    private int techScoreRank;

    private int battleScoreMax;

    private int battleScoreRank;

    private int platinumScoreMax;

    private int maxComboCount;

    private int maxOverKill;

    private int maxTeamOverKill;

    @JsonProperty("isFullBell")
    private boolean isFullBell;

    @JsonProperty("isFullCombo")
    private boolean isFullCombo;

    @JsonProperty("isAllBreake")
    private boolean isAllBreake;

    @JsonProperty("isLock")
    private boolean isLock;

    private int clearStatus;

    private boolean isStoryWatched;

    public UserMusicDetail(UserData userData) {
        this.user = userData;
    }
}
