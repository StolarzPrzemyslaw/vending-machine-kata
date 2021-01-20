package org.codecool.vendingmachine.model;

public class Product {
    private final ProductType productType;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getType() {
        return productType;
    }
}
