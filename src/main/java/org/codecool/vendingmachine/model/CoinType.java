package org.codecool.vendingmachine.model;

import java.math.BigDecimal;

public enum CoinType {
    PENNY(new BigDecimal("0.01"), 1) {
        @Override
        public boolean isValid() {
            return false;
        }
    },
    NICKEL(new BigDecimal("0.05"), 2),
    DIME(new BigDecimal("0.1"), 3),
    QUARTER(new BigDecimal("0.25"), 4);

    private final BigDecimal value;
    private final int weight;

    CoinType(BigDecimal value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isValid() {
        return true;
    }
}
