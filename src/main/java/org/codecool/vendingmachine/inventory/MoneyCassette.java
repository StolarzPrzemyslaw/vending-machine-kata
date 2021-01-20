package org.codecool.vendingmachine.inventory;

import org.codecool.vendingmachine.model.Coin;
import org.codecool.vendingmachine.model.CoinType;

import java.util.HashMap;
import java.util.Map;

public class MoneyCassette {
    private final Map<CoinType, Integer> coins = new HashMap<>();

    public void add(Coin coin) {
        if (coins.containsKey(coin.getType())) {
            coins.put(coin.getType(), coins.get(coin.getType()) + 1);
        } else {
            coins.put(coin.getType(), 1);
        }
    }

    public void remove(CoinType coinType) throws Exception {
        if (coins.containsKey(coinType)) {
            coins.put(coinType, coins.get(coinType) - 1);
            if (coins.get(coinType) == 0) {
                coins.remove(coinType);
            }
        } else
            throw new Exception("No such coin " + coinType);
    }
}
