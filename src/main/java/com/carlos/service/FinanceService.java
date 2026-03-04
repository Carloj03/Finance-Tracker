package com.carlos;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FinanceService {

    // Newest first (add at index 0)
    private final List<Transaction> transactions = new ArrayList<>();

    // Separate category sets per type
    private final Map<TransactionType, Set<String>> categories = new HashMap<>();

    public FinanceService() {
        categories.put(TransactionType.INCOME, new HashSet<>());
        categories.put(TransactionType.EXPENSE, new HashSet<>());
    }

    // --------------------
    // Category Management
    // --------------------

    public void addCategory(TransactionType type, String category) {
        categories.get(type).add(category);
    }

    public void removeCategory(TransactionType type, String category) {
        categories.get(type).remove(category);

        // Remove associated transactions
        transactions.removeIf(t ->
                t.getType() == type &&
                t.getCategory().equals(category)
        );
    }

    public List<String> getCategories(TransactionType type) {
        return new ArrayList<>(categories.get(type));
    }

    // --------------------
    // Transaction Management
    // --------------------

    public void addTransaction(Transaction transaction) {
        transactions.add(0, transaction); // newest first
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public List<Transaction> getTransactions(TransactionType type,
                                             String category) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .filter(t -> t.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactions(TransactionType type,
                                             String category,
                                             LocalDate start,
                                             LocalDate end) {

        return transactions.stream()
                .filter(t -> t.getType() == type)
                .filter(t -> t.getCategory().equals(category))
                .filter(t -> !t.getDate().isBefore(start)
                          && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // --------------------
    // Removal Filtering
    // --------------------

    public List<Transaction> findByName(TransactionType type,
                                        String category,
                                        String name) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .filter(t -> t.getCategory().equals(category))
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByDate(List<Transaction> list,
                                          LocalDate date) {
        return list.stream()
                .filter(t -> t.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByAmount(List<Transaction> list,
                                            double amount) {
        return list.stream()
                .filter(t -> t.getAmount() == amount)
                .collect(Collectors.toList());
    }

    public List<Transaction> filterBySource(List<Transaction> list,
                                            String source) {
        return list.stream()
                .filter(t -> t.getSource().equalsIgnoreCase(source))
                .collect(Collectors.toList());
    }
}