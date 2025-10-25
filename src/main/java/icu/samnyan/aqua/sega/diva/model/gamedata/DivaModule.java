package icu.samnyan.aqua.sega.diva.model.gamedata;

import icu.samnyan.aqua.sega.diva.model.Internalizable;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "DivaModule")
@Table(name = "diva_module")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivaModule implements Serializable, Internalizable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private String name;

    private int price;

    private LocalDateTime releaseDate;

    private LocalDateTime endDate;

    private int sortOrder;

    @Override
    public String toInternal() {
        return id + ",0," +
            URIEncoder.encode(name) + "," +
            price + "," +
            DivaDateTimeUtil.format(releaseDate) + "," +
            DivaDateTimeUtil.format(endDate) + "," +
            sortOrder;
    }
}
