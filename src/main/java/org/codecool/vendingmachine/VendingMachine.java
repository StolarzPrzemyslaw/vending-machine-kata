package org.codecool.vendingmachine;

import org.codecool.vendingmachine.display.DisplayManager;
import org.codecool.vendingmachine.inventory.Inventory;
import org.codecool.vendingmachine.inventory.MoneyCassette;
import org.codecool.vendingmachine.model.Coin;
import org.codecool.vendingmachine.model.CoinType;
import org.codecool.vendingmachine.model.ProductType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class VendingMachine {
    private final Inventory inventory;
    private final MoneyCassette moneyCassette;
    private final MoneyCassette creditCassette;
    private final DisplayManager displayManager;

    public VendingMachine() {
        this.inventory = new Inventory();
        this.moneyCassette = new MoneyCassette();
        this.displayManager = new DisplayManager(this);
        this.creditCassette = new MoneyCassette();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MoneyCassette getMoneyCassette() {
        return moneyCassette;
    }

    public MoneyCassette getCreditCassette() {
        return creditCassette;
    }

    public void run() {
        displayManager.run();
    }

    public void acceptCoin(Coin coin) {
        if (coin.isValid()) {
            creditCassette.add(coin);
        }
    }

    public void returnCoins() {
        creditCassette.empty();
    }

    public boolean buyProduct(ProductType productType) {
        if (inventory.isSoldOut(productType)) {
            displayManager.soldOut(productType);
            return false;
        }
        if (creditCassette.getTotalAmount().compareTo(productType.getPrice()) < 0) {
            displayManager.notEnoughMoney(productType);
            return false;
        }
//        if (creditCassette.getTotalAmount().compareTo(productType.getPrice()) > 0 &&
//                !moneyCassette.getCoins().containsKey(CoinType.NICKEL) ||
//            productType.equals(ProductType.CANDY) &&
//            (!moneyCassette.getCoins().containsKey(CoinType.DIME) || moneyCassette.getCoins().getOrDefault(CoinType.NICKEL, 0) < 2)) {
//            displayManager.exactChange();
//            return false;
//        }
        finalizePurchase(productType);
        return true;
    }

    private void finalizePurchase(ProductType productType) {
        makeChange(productType);
        inventory.remove(productType);
        joinCassettes();
        creditCassette.empty();
        displayManager.boughtProduct(productType);
    }

    private void joinCassettes() {
        creditCassette.getCoins().
                forEach((key, value) -> moneyCassette.getCoins().merge(key, value, Integer::sum));
    }

    public void makeChange(ProductType productType) {
        BigDecimal change = creditCassette.getTotalAmount().subtract(productType.getPrice());
        Map<CoinType, Integer> changeMap = new HashMap<>();
        if (change.compareTo(new BigDecimal("0")) == 0) {
            displayManager.showChange(changeMap);
            return;
        }
        CoinType smallestCreditCoin = moneyCassette.getSmallest();

        while (change.compareTo(new BigDecimal("0")) != 0) {
            change = change.subtract(smallestCreditCoin.getValue());
            if (changeMap.containsKey(smallestCreditCoin)) {
                changeMap.put(smallestCreditCoin, changeMap.get(smallestCreditCoin) + 1);
            } else {
                changeMap.put(smallestCreditCoin, 1);
            }
            moneyCassette.remove(smallestCreditCoin);
        }
        displayManager.showChange(changeMap);
    }

    public int computeChange(int[] coins, int[] count, int sum) {

        int n = coins.length;
        int[][] table = new int[n + 1][sum + 1];

        table[0][0] = 1;
        for (int j = 1; j <= n; j++) {
            for (int i = 0; i <= sum; i++) {
                table[j][i] += table[j - 1][i];
            }
            System.out.println(Arrays.deepToString(table));
            for (int k = 1; k <= count[j - 1]; k++) {
                int initial = coins[j - 1] * k;
                for (int i = initial; i <= sum; i++) {
                    table[j][i] += table[j - 1][i - initial];
                }
            }
        }
//        for(int i = 0; i <= n; i++) {
//            for(int j = 0; j <= sum; j++) {
//                System.out.print(table[i][j] + " ");
//            }
//            System.out.println("");
//        }

        return table[n][sum];
    }
}

