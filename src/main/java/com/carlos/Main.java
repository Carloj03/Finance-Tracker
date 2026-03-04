// Handles input and output for the application.

package com.carlos;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final Scanner scnr = new Scanner(System.in);
    private final FinanceService service = new FinanceService();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            TransactionType type = selectType();
            if (type == null) break;
            handleCategoryMenu(type);
        }
    }

    private TransactionType selectType() {
        while (true) {
            System.out.print("Select type (income, expense, exit): ");
            String input = scnr.nextLine().toLowerCase();

            switch (input) {
                case "income":
                    return TransactionType.INCOME;
                case "expense":
                    return TransactionType.EXPENSE;
                case "exit":
                    return null;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private void handleCategoryMenu(TransactionType type) {
        while (true) {

            List<String> categories = service.getCategories(type);

            if (categories.isEmpty()) {
                System.out.println("No categories exist. You must add one.");
                addCategory(type);
                continue;
            }

            System.out.print("Category options: view, add, remove, back: ");
            String command = scnr.nextLine().toLowerCase();

            switch (command) {
                case "view":
                    handleViewCategory(type);
                    break;
                case "add":
                    addCategory(type);
                    break;
                case "remove":
                    removeCategory(type);
                    break;
                case "back":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void addCategory(TransactionType type) {
        System.out.print("Enter category name (or cancel): ");
        String name = scnr.nextLine();
        if (name.equalsIgnoreCase("cancel")) return;
        service.addCategory(type, name);
    }

    private void removeCategory(TransactionType type) {
        System.out.print("Enter category name to remove (or cancel): ");
        String name = scnr.nextLine();
        if (name.equalsIgnoreCase("cancel")) return;
        service.removeCategory(type, name);
    }

    private void handleViewCategory(TransactionType type) {
        System.out.print("Enter category name: ");
        String category = scnr.nextLine();

        System.out.print("Enter timeframe (YYYY-MM-DD to YYYY-MM-DD) or press enter for all: ");
        String timeframe = scnr.nextLine();

        List<Transaction> transactions;

        if (timeframe.isBlank()) {
            transactions = service.getTransactions(type, category);
        } else {
            String[] parts = timeframe.split(" to ");
            LocalDate start = LocalDate.parse(parts[0]);
            LocalDate end = LocalDate.parse(parts[1]);
            transactions = service.getTransactions(type, category, start, end);
        }

        for (Transaction t : transactions) {
            System.out.println(t);
        }

        handleTransactionMenu(type, category);
    }

    private void handleTransactionMenu(TransactionType type, String category) {
        while (true) {
            System.out.print("Transaction options: view, add, remove, back: ");
            String command = scnr.nextLine().toLowerCase();

            switch (command) {
                case "view":
                    System.out.println("View not implemented yet.");
                    break;
                case "add":
                    addTransaction(type, category);
                    break;
                case "remove":
                    removeTransaction(type, category);
                    break;
                case "back":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void addTransaction(TransactionType type, String category) {

        System.out.print("Name (or cancel): ");
        String name = scnr.nextLine();
        if (name.equalsIgnoreCase("cancel")) return;

        System.out.print("Amount (or cancel): ");
        String amountInput = scnr.nextLine();
        if (amountInput.equalsIgnoreCase("cancel")) return;
        double amount = Double.parseDouble(amountInput);

        System.out.print("Date (YYYY-MM-DD or cancel): ");
        String dateInput = scnr.nextLine();
        if (dateInput.equalsIgnoreCase("cancel")) return;
        LocalDate date = LocalDate.parse(dateInput);

        System.out.print("Source (or cancel): ");
        String source = scnr.nextLine();
        if (source.equalsIgnoreCase("cancel")) return;

        service.addTransaction(
                new Transaction(name, amount, date, source, type, category)
        );
    }

    private void removeTransaction(TransactionType type, String category) {

        System.out.print("Enter name (or cancel): ");
        String name = scnr.nextLine();
        if (name.equalsIgnoreCase("cancel")) return;

        List<Transaction> matches =
                service.findByName(type, category, name);

        if (matches.size() > 1) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate d = LocalDate.parse(scnr.nextLine());
            matches = service.filterByDate(matches, d);
        }

        if (matches.size() > 1) {
            System.out.print("Enter amount: ");
            double a = Double.parseDouble(scnr.nextLine());
            matches = service.filterByAmount(matches, a);
        }

        if (matches.size() > 1) {
            System.out.print("Enter source: ");
            String s = scnr.nextLine();
            matches = service.filterBySource(matches, s);
        }

        if (!matches.isEmpty()) {
            service.removeTransaction(matches.get(0));
        }
    }
}