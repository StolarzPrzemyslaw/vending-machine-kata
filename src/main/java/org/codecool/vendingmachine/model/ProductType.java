package org.codecool.vendingmachine.model;

import java.math.BigDecimal;

public enum ProductType {
    COLA(new BigDecimal("1"), 1),
    CHIPS(new BigDecimal("0.5"), 2),
    CANDY(new BigDecimal("0.65"), 3);

    private final BigDecimal price;
    private final int code;

    ProductType(BigDecimal price, int code) {
        this.price = price;
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getCode() {
        return code;
    }
}
