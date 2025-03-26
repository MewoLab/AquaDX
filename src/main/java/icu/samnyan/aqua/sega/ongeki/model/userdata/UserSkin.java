package icu.samnyan.aqua.sega.ongeki.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "OngekiUserSkin")
@Table(name = "ongeki_user_skin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    //TODO: should be updated on net when changing skin
    private boolean isValid;

    private int deckId;

    private int cardId1;

    private int cardId2;

    private int cardId3;

    public UserSkin(UserData userData) {
        this.user = userData;
    }
}
