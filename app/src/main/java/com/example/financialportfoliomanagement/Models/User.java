package com.example.financialportfoliomanagement.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String user_id;
    private List<String> watch_list_symbols;
    private List<String> transactions;
    private List<String> riskScore;

    public User(String name, String user_id, List<String> watch_list_symbols, List<String> transactions, List<String> riskScore, String category) {
        this.name = name;
        this.user_id = user_id;
        this.watch_list_symbols = watch_list_symbols;
        this.transactions = transactions;
        this.riskScore = riskScore;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;


    public User() {
    }

    public User(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    public User(String name, List<String> transactions, String user_id, List<String> watch_list_symbols) {
        this.user_id = user_id;
        this.name = name;
        this.watch_list_symbols = watch_list_symbols;
        this.transactions = transactions;
    }

    public User(String name, List<String> riskScore, List<String> transactions, String user_id, List<String> watch_list_symbols) {
        this.name = name;
        this.user_id = user_id;
        this.watch_list_symbols = watch_list_symbols;
        this.transactions = transactions;
        this.riskScore = riskScore;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setRiskScore(List<String> riskScore) {
        this.riskScore = riskScore;
    }

    public List<String> getRiskScore() {
        return riskScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setWatch_list_symbols(List<String> watch_list_symbols) {
        this.watch_list_symbols = watch_list_symbols;
    }


    public List<String> getWatch_list_symbols() {
        return watch_list_symbols;
    }

    public boolean add_watch_list_item(String symbol) {
        if (this.watch_list_symbols != null) {
            if (this.watch_list_symbols.size() == 20) return false;
            if (!this.watch_list_symbols.contains(symbol)) {
                watch_list_symbols.add(symbol);
                return true;
            }

            return false;
        } else {
            this.watch_list_symbols = new ArrayList<>();
            watch_list_symbols.add(symbol);
            return true;
        }

    }

    public boolean add_transaction_item(String transaction) {
        if (this.transactions != null) {

            if (!this.transactions.contains(transaction)) {
                transactions.add(transaction);
                return true;
            }

            return false;
        } else {
            this.transactions = new ArrayList<>();
            transactions.add(transaction);
            return true;
        }
    }

    public boolean delete_watch_list_item(int position) {
        if (this.watch_list_symbols != null) {
            if (this.watch_list_symbols.get(position) != null) {
                this.watch_list_symbols.remove(position);
                return true;
            }
            return false;
        }
        return false;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }



}
