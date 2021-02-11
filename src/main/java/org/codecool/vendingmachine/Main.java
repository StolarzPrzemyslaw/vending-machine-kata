package org.codecool.vendingmachine;

import org.codecool.vendingmachine.inventory.Inventory;
import org.codecool.vendingmachine.inventory.MoneyCassette;
import org.codecool.vendingmachine.model.Coin;
import org.codecool.vendingmachine.model.CoinType;
import org.codecool.vendingmachine.model.Product;
import org.codecool.vendingmachine.model.ProductType;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) {
        VendingMachine machine = new VendingMachine();
        Inventory inventory = machine.getInventory();
        for (int i = 0; i < 5; i++) {
            inventory.add(new Product(ProductType.CHIPS));
            inventory.add(new Product(ProductType.CANDY));
            inventory.add(new Product(ProductType.COLA));
        }
        inventory.remove(ProductType.CHIPS);

        MoneyCassette moneyCassette = machine.getMoneyCassette();
        for (int i = 0; i < 10; i++) {
//            moneyCassette.add(new Coin(CoinType.NICKEL));
//            moneyCassette.add(new Coin(CoinType.DIME));
//            moneyCassette.add(new Coin(CoinType.QUARTER));
        }

        machine.run();

    }
}
