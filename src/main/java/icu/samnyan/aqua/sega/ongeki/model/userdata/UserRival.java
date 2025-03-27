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
@Entity(name = "OngekiUserRival")
@Table(name = "ongeki_user_rival")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRival implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserData user;

    @JoinColumn(name = "rival_user_ext_id")
    @JsonProperty("rivalUserId")
    private long rivalUserExtId;
}