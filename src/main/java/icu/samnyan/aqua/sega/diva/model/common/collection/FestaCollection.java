package icu.samnyan.aqua.sega.diva.model.common.collection;

import icu.samnyan.aqua.sega.diva.model.gamedata.Festa;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.Data;

import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
public class FestaCollection {
    private Festa firstFesta = new Festa();
    private Festa secondFesta = new Festa();

    public FestaCollection(Festa firstFesta, Festa secondFesta) {
        this.firstFesta = firstFesta;
        this.secondFesta = secondFesta;
    }

    public FestaCollection(Festa firstFesta) {
        this.firstFesta = firstFesta;
    }

    public FestaCollection(List<Festa> festas) {
        if (festas.size() >= 2) {
            this.firstFesta = festas.get(0);
            this.secondFesta = festas.get(1);
        } else if (festas.size() == 1) {
            this.firstFesta = festas.get(0);
        }
    }

    public String getIds() {
        return this.firstFesta.getId() + "," + this.secondFesta.getId();
    }

    public String getNames() {
        return URIEncoder.encode(this.firstFesta.getName()) + "," + URIEncoder.encode(this.secondFesta.getName());
    }

    public String getKinds() {
        return this.firstFesta.getKind().getValue() + "," + this.secondFesta.getKind().getValue();
    }

    public String getDiffs() {
        return this.firstFesta.getDifficulty().getValue() + "," + this.secondFesta.getDifficulty().getValue();
    }

    public String getPvIds() {
        return this.firstFesta.getPvList() + "," + this.secondFesta.getPvList();
    }

    public String getAttr() {
        return this.firstFesta.getAttributes() + "," + this.secondFesta.getAttributes();
    }

    public String getAddVps() {
        return this.firstFesta.getAddVP() + "," + this.secondFesta.getAddVP();
    }

    public String getVpMultipliers() {
        return this.firstFesta.getVpMultiplier() + "," + this.secondFesta.getVpMultiplier();
    }

    public String getStarts() {
        return DivaDateTimeUtil.getString(this.firstFesta.getStart()) + "," + DivaDateTimeUtil.getString(this.secondFesta.getStart());
    }

    public String getEnds() {
        return DivaDateTimeUtil.getString(this.firstFesta.getEnd()) + "," + DivaDateTimeUtil.getString(this.secondFesta.getEnd());
    }

    public String getLastUpdateTime() {
        return DivaDateTimeUtil.getString(this.firstFesta.getCreateDate().compareTo(this.secondFesta.getCreateDate()) < 0 ? this.firstFesta.getCreateDate() : this.secondFesta.getCreateDate());
    }
}
