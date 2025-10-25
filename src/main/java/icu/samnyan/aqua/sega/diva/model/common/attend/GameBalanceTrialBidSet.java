package icu.samnyan.aqua.sega.diva.model.common.attend;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
@AllArgsConstructor
public class GameBalanceTrialBidSet {
    private GameBalanceTrialBid clear;
    private GameBalanceTrialBid great;
    private GameBalanceTrialBid excellent;
    private GameBalanceTrialBid perfect;

    public List<Integer> getArr() {
        Integer[] arr = new Integer[]{
            clear.getPay(),
            clear.getWin(),
            great.getPay(),
            great.getWin(),
            excellent.getPay(),
            excellent.getWin(),
            perfect.getPay(),
            perfect.getWin(),
        };
        return new LinkedList<>(Arrays.asList(arr));
    }
}
