package com.example.financialportfoliomanagement.Models;

public class StockItem {
    public String symbol;
    public String name;
    public String low, high, open, close;

    public StockItem(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public StockItem(String symbol, String name, String low, String high, String open, String close) {
        this.symbol = symbol;
        this.name = name;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
    }
}
