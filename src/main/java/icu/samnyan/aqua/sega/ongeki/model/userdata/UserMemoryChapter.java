package icu.samnyan.aqua.sega.ongeki.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "OngekiUserMemoryChapter")
@Table(name = "ongeki_user_memory_chapter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMemoryChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    private int chapterId;

    private int jewelCount;

    private int lastPlayMusicCategory;

    private int lastPlayMusicId;

    private int lastPlayMusicLevel;

    @JsonProperty("isDialogWatched")
    private boolean isDialogWatched;

    @JsonProperty("isStoryWatched")
    private boolean isStoryWatched;

    @JsonProperty("isBossWatched")
    private boolean isBossWatched;

    @JsonProperty("isEndingWatched")
    private boolean isEndingWatched;

    @JsonProperty("isClear")
    private boolean isClear;

    private int gaugeId;

    private int gaugeNum;

    public UserMemoryChapter(UserData userData) {
        this.user = userData;
    }
}
