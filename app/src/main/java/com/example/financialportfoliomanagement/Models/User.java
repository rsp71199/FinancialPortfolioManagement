package com.example.financialportfoliomanagement.Models;

import java.util.List;

public class User {
    private String name;
    private String user_id;
    private List<String> watch_list_symbols;

    public User() {
    }

    public User(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    public User(String user_id, String name, List<String> watch_list_symbols) {
        this.user_id = user_id;
        this.name = name;
        this.watch_list_symbols = watch_list_symbols;
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
            if (!this.watch_list_symbols.contains(symbol)) {
                watch_list_symbols.add(symbol);
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
