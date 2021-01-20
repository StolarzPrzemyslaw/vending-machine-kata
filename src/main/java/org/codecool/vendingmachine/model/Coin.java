package org.codecool.vendingmachine.model;

public class Coin {
    private final CoinType coinType;

    public Coin(CoinType coinType) {
        this.coinType = coinType;
    }

    public CoinType getType() {
        return coinType;
    }

    public boolean isValid() {
        return coinType.isValid();
    }

}
