package com.example.financialportfoliomanagement.Models;

public class SearchResult extends StockItem {
    public boolean added_to_list = false;

    public SearchResult(String name, String symbol) {
        super(symbol, name);
    }

    public SearchResult(String name, String symbol, boolean added_to_list) {
        super(symbol, name);
        this.added_to_list = added_to_list;
    }
}
