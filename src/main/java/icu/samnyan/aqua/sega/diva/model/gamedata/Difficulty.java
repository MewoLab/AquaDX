package icu.samnyan.aqua.sega.diva.model.gamedata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "DivaPvLevel")
@Table(name = "diva_pv_info_level")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Difficulty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pv_id")
    private Pv pv;

    private int edition;

    private String level;

    private int version;

    private String diff;
}
