package icu.samnyan.aqua.sega.chusan.model.userdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "ChusanUserDuel")
@Table(name = "chusan_user_duel", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "duel_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDuel extends Chu3UserEntity {
    @Column(name = "duel_id")
    private int duelId;

    private int progress;

    private int point;

    @JsonProperty("isClear")
    private boolean isClear;

    private LocalDateTime lastPlayDate;

    private int param1;

    private int param2;

    private int param3;

    private int param4;

    public UserDuel(Chu3UserData userData) {
        setUser(userData);
    }
}
