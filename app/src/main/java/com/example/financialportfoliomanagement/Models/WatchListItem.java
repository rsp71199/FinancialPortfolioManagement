package com.example.financialportfoliomanagement.Models;

public class WatchListItem extends StockItem {
    public WatchListItem(String symbol, String name) {
        super(symbol, name);

    }

    public WatchListItem(String symbol, String name, Float low, Float high, Float open, Float close) {
        super(symbol, name, low, high, open, close);
    }

}
