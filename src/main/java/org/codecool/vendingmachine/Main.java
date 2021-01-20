package org.codecool.vendingmachine;

import org.codecool.vendingmachine.model.Product;
import org.codecool.vendingmachine.model.ProductType;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) {
        System.out.println(new Product(ProductType.CHIPS).getType());
    }
}
