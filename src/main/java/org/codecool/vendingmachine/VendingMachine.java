package org.codecool.vendingmachine;

import org.codecool.vendingmachine.display.DisplayManager;
import org.codecool.vendingmachine.inventory.Inventory;
import org.codecool.vendingmachine.inventory.MoneyCassette;
import org.codecool.vendingmachine.model.Coin;
import org.codecool.vendingmachine.model.CoinType;
import org.codecool.vendingmachine.model.ProductType;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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
        if (makeChange(productType)) {
            inventory.remove(productType);
            joinCassettes();
            creditCassette.empty();
            displayManager.boughtProduct(productType);
            return;
        }
        displayManager.exactChange();
    }

    private void joinCassettes() {
        creditCassette.getCoins().
                forEach((key, value) -> moneyCassette.getCoins().merge(key, value, Integer::sum));
    }

//    public void makeChange(ProductType productType) {
//        BigDecimal change = creditCassette.getTotalAmount().subtract(productType.getPrice());
//        Map<CoinType, Integer> changeMap = new HashMap<>();
//        if (change.compareTo(new BigDecimal("0")) == 0) {
//            displayManager.showChange(changeMap);
//            return;
//        }
//        CoinType smallestCreditCoin = moneyCassette.getSmallest();
//
//        while (change.compareTo(new BigDecimal("0")) != 0) {
//            change = change.subtract(smallestCreditCoin.getValue());
//            if (changeMap.containsKey(smallestCreditCoin)) {
//                changeMap.put(smallestCreditCoin, changeMap.get(smallestCreditCoin) + 1);
//            } else {
//                changeMap.put(smallestCreditCoin, 1);
//            }
//            moneyCassette.remove(smallestCreditCoin);
//        }
//        displayManager.showChange(changeMap);
//    }


    private void computeChange(List<Integer> partialResult, int currentSum, int coinIndex, List<Integer> coins, int totalSum, List<List<Integer>> result){
        int oldSum = currentSum;

        if (coinIndex == coins.size())
            return;

        int value = coins.get(coinIndex);

        while (currentSum < totalSum){
            currentSum += value;
            partialResult.add(value);
        }

        if (currentSum == totalSum){
            result.add(new ArrayList<>(partialResult));
        }

        while (currentSum > oldSum) {
            currentSum -= value;
            partialResult.remove(partialResult.size() - 1);

            if (currentSum >= 0) {
                computeChange(partialResult, currentSum, coinIndex + 1, coins, totalSum, result);
            }
        }
    }

    public boolean makeChange(ProductType productType) {
        BigDecimal change = creditCassette.getTotalAmount().subtract(productType.getPrice());
        Map<CoinType, Integer> changeMap = new HashMap<>();
        if (change.compareTo(new BigDecimal("0")) == 0) {
            displayManager.showChange(changeMap);
            return true;
        }

        int totalSum = change.multiply(new BigDecimal("100")).intValue();
        List<Integer> coins = Arrays.stream(CoinType.values()).
                filter(CoinType::isValid).
                map(value -> value.getValue().multiply(new BigDecimal("100")).intValue()).collect(Collectors.toList());
        List<List<Integer>> result = new ArrayList<>();
        computeChange(new ArrayList<>(), 0, 0, coins, totalSum, result);
        result.sort(Comparator.comparingInt(List::size));
        for (List<Integer> res : result) {
            List<CoinType> coinTypeList = convertToCoinTypeList(convertToBigDecimalList(res));
            changeMap = convertToCoinMap(coinTypeList);
            System.out.println(changeMap);
            if (isChangeAvailable(changeMap)) {
                for (CoinType coinType : coinTypeList) {
                    moneyCassette.remove(coinType);
                }
                displayManager.showChange(changeMap);
                return true;
            }
        }

        return false;
    }

    private List<BigDecimal> convertToBigDecimalList(List<Integer> result) {
        return result.stream().
                map(BigDecimal::new).
                map(value -> value.divide(new BigDecimal(100))).
                collect(Collectors.toList());
    }

    private List<CoinType> convertToCoinTypeList(List<BigDecimal> result) {
        List<CoinType> coinTypeList = new ArrayList<>();
        for (CoinType coinType : CoinType.values()) {
            for (BigDecimal value : result) {
                if (value.compareTo(coinType.getValue()) == 0) {
                    coinTypeList.add(coinType);
                }
            }
        }
        return coinTypeList;
    }

    private Map<CoinType, Integer> convertToCoinMap(List<CoinType> coinTypeList) {
        Map<CoinType, Integer> coinMap = new HashMap<>();
        for (CoinType coinType : coinTypeList) {
            if (coinMap.containsKey(coinType)) {
                coinMap.put(coinType, coinMap.get(coinType) + 1);
            } else {
                coinMap.put(coinType, 1);
            }
        }
        return coinMap;
    }

    private boolean isChangeAvailable(Map<CoinType, Integer> coinMap) {
            for (Map.Entry<CoinType, Integer> entry : coinMap.entrySet()) {
                if (entry.getValue() > moneyCassette.getCoins().getOrDefault(entry.getKey(), 0)) {
                    return false;
                }
            }
        return true;
    }


//    public int computeChange(int[] coins, int[] count, int sum) {
//
//        int n = coins.length;
//        int[][] table = new int[n + 1][sum + 1];
//
//        table[0][0] = 1;
//        for (int j = 1; j <= n; j++) {
//            for (int i = 0; i <= sum; i++) {
//                table[j][i] += table[j - 1][i];
//            }
//            System.out.println(Arrays.deepToString(table));
//            for (int k = 1; k <= count[j - 1]; k++) {
//                int initial = coins[j - 1] * k;
//                for (int i = initial; i <= sum; i++) {
//                    table[j][i] += table[j - 1][i - initial];
//                }
//            }
//        }
////        for(int i = 0; i <= n; i++) {
////            for(int j = 0; j <= sum; j++) {
////                System.out.print(table[i][j] + " ");
////            }
////            System.out.println("");
////        }
//
//        return table[n][sum];
//    }
}

