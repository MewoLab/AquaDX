package icu.samnyan.aqua.sega.diva.model.common.attend;

import icu.samnyan.aqua.sega.diva.model.Internalizable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
@NoArgsConstructor
public class GameBalanceParameter implements Internalizable {

    private int name_change_price = 100;
    private GameBalanceTrialBidSet easy_trials = new GameBalanceTrialBidSet(
        new GameBalanceTrialBid(5, 10),
        new GameBalanceTrialBid(10, 25),
        new GameBalanceTrialBid(20, 50),
        new GameBalanceTrialBid(30, 90)
    );
    private GameBalanceTrialBidSet normal_trials = new GameBalanceTrialBidSet(
        new GameBalanceTrialBid(5, 10),
        new GameBalanceTrialBid(10, 25),
        new GameBalanceTrialBid(20, 50),
        new GameBalanceTrialBid(30, 90)
    );
    private GameBalanceTrialBidSet hard_trials = new GameBalanceTrialBidSet(
        new GameBalanceTrialBid(5, 10),
        new GameBalanceTrialBid(10, 25),
        new GameBalanceTrialBid(20, 50),
        new GameBalanceTrialBid(30, 90)
    );
    private GameBalanceTrialBidSet extreme_trials = new GameBalanceTrialBidSet(
        new GameBalanceTrialBid(5, 10),
        new GameBalanceTrialBid(10, 25),
        new GameBalanceTrialBid(20, 50),
        new GameBalanceTrialBid(30, 90)
    );
    private GameBalanceTrialBidSet extra_extreme_trials = new GameBalanceTrialBidSet(
        new GameBalanceTrialBid(5, 10),
        new GameBalanceTrialBid(10, 25),
        new GameBalanceTrialBid(20, 50),
        new GameBalanceTrialBid(30, 90)
    );

    @Override
    public String toInternal() {
        Integer[] arr = new Integer[]{
            this.name_change_price,
            0, // unkn
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            2,
            3,
            4, // unkn
            1,
            1,
            1,
            3,
            4,
            5, // unkn
            1,
            1,
            1,
            4,
            5,
            6, // unkn
            1,
            1,
            1,
            5,
            6,
            7, // unkn
            4,
            4,
            4,
            9,
            10,
            14 // unkn
        };
        List<Integer> list = new LinkedList<>(Arrays.asList(arr));
        list.addAll(easy_trials.getArr());
        list.addAll(normal_trials.getArr());
        list.addAll(hard_trials.getArr());
        list.addAll(extreme_trials.getArr());
        list.addAll(extra_extreme_trials.getArr());
        list.add(10);
        list.add(30);
        list.addAll(Collections.nCopies(100, 0));
        return list.stream().limit(100).map(Object::toString).collect(Collectors.joining(","));
    }
}
