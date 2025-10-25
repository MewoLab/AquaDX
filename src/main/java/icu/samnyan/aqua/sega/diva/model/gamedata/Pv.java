package icu.samnyan.aqua.sega.diva.model.gamedata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "DivaPvInfo")
@Table(name = "diva_pv_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pv implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer pvId;

    private Integer bpm;

    private String songName;

    private String songNameEng;

    private String songNameReading;

    private String arranger;

    private String lyrics;

    private String music;

    private Integer performerNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pv")
    @MapKey(name = "diff")
    private Map<String, Difficulty> difficulty;
}
