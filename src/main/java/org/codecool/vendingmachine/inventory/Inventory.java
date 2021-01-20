package org.codecool.vendingmachine.inventory;

import org.codecool.vendingmachine.model.Product;
import org.codecool.vendingmachine.model.ProductType;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<ProductType, Integer> products = new HashMap<>();

    public void add(Product product) {
        if (products.containsKey(product.getType())) {
            products.put(product.getType(), products.get(product.getType()) + 1);
        } else {
            products.put(product.getType(), 1);
        }
    }

    public void remove(ProductType productType) throws Exception {
        if (products.containsKey(productType)) {
            products.put(productType, products.get(productType) - 1);
            if (products.get(productType) == 0) {
                products.remove(productType);
            }
        } else
            throw new Exception(productType + " sold out");
    }

    public boolean isSoldOut(Product product) {
        return !products.containsKey(product.getType());
    }
}
