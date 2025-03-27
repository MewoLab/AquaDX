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
@Entity(name = "OngekiUserEventMap")
@Table(name = "ongeki_user_event_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    private int eventId;

    private int mapId;

    private String mapData;

    private int totalPoint;

    private int totalUsePoint;

    public UserEventMap(UserData userData) {
        this.user = userData;
    }
}
