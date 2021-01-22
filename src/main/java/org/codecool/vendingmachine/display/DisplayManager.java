package org.codecool.vendingmachine.display;

import org.codecool.vendingmachine.VendingMachine;
import org.codecool.vendingmachine.model.Coin;
import org.codecool.vendingmachine.model.CoinType;
import org.codecool.vendingmachine.model.ProductType;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class DisplayManager {
    Scanner scanner;
    PrintStream out;
    VendingMachine vendingMachine;

    public DisplayManager(VendingMachine vendingMachine) {
        this.scanner = new Scanner(System.in);
        this.out = new PrintStream(System.out);
        this.vendingMachine = vendingMachine;
    }

    public void run() {
        moneyMenu();
    }

    private void moneyMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println(vendingMachine.getInventory());
            System.out.println(vendingMachine.getMoneyCassette());

            moneyMenuInfo();
            switch (moneyChoice()) {
                case '1':
                    vendingMachine.acceptCoin(new Coin(CoinType.PENNY));
                    break;
                case '2':
                    vendingMachine.acceptCoin(new Coin(CoinType.NICKEL));
                    break;
                case '3':
                    vendingMachine.acceptCoin(new Coin(CoinType.DIME));
                    break;
                case '4':
                    vendingMachine.acceptCoin(new Coin(CoinType.QUARTER));
                    break;
                case '5':
                    productMenu();
                    break;
                case '6':
                    vendingMachine.returnCoins();
                    break;
                case '7':
                    isRunning = false;
                    break;
            }
        }
    }

    private void moneyMenuInfo() {
        out.println("Insert coin");
        out.println("Credit: " + vendingMachine.getCreditCassette().getTotalAmount());
        out.println("1. " + CoinType.PENNY);
        out.println("2. " + CoinType.NICKEL);
        out.println("3. " + CoinType.DIME);
        out.println("4. " + CoinType.QUARTER);
        out.println("5. Choose product");
        out.println("6. Return coins");
        out.println("7. Quit");
    }

    private char moneyChoice() {
        String options = "1234567";
        String message = "Choose option: ";
        return choice(options, message);
    }

    private char productChoice() {
        String options = "1234";
        String message = "Choose product: ";
        return choice(options, message);
    }

    private char choice(String options, String message) {
        String line;
        do {
            out.print(message);
            line = scanner.nextLine();
        } while (!(line.length() == 1 && options.contains(line)));
        return line.charAt(0);
    }

    private void productMenu() {
        boolean isRunning = true;
        boolean isBought;
        while (isRunning) {
            System.out.println(vendingMachine.getInventory());
            System.out.println(vendingMachine.getMoneyCassette());
            productMenuInfo();
            switch (productChoice()) {
                case '1':
                    isBought = vendingMachine.buyProduct(ProductType.COLA);
                    isRunning = !isBought;
                    break;

                case '2':
                    isBought = vendingMachine.buyProduct(ProductType.CHIPS);
                    isRunning = !isBought;
                    break;
                case '3':
                    isBought = vendingMachine.buyProduct(ProductType.CANDY);
                    isRunning = !isBought;
                    break;
                case '4':
                    isRunning = false;
                    break;
            }
        }
    }

    private void productMenuInfo() {
        out.println(ProductType.COLA.getCode() + ". " + ProductType.COLA);
        out.println(ProductType.CHIPS.getCode() + ". " + ProductType.CHIPS);
        out.println(ProductType.CANDY.getCode() + ". " + ProductType.CANDY);
        out.println("4. Insert coin");
    }

    public void soldOut(ProductType productType) {
        printMessage(productType + " is sold out");
    }

    public void notEnoughMoney(ProductType productType) {
        printMessage("Not enough money to buy " + productType);
    }

    public void boughtProduct(ProductType productType) {
        printMessage("You bought " + productType);
    }

    public void exactChange() {
        printMessage("Exact change only");
    }

    public void showChange(Map<CoinType, Integer> change) {
        printMessage("Your change is " + change.toString());
    }

    private void printMessage(String message) {
        out.println(message);
    }

}
