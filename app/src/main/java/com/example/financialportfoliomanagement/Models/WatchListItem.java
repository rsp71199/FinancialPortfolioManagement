package com.example.financialportfoliomanagement.Models;

public class WatchListItem extends StockItem {

    public String change;
    public String prev_close;

    public WatchListItem(String symbol, String name) {
        super(symbol, name);

    }

    public WatchListItem(String symbol, String name, String low, String high, String open, String close) {
        super(symbol, name, low, high, open, close);
    }

    public WatchListItem(String symbol, String name, String low, String high, String open, String close, String change, String prev_close) {
        super(symbol, name, low, high, open, close);
        this.change = change;
        this.prev_close = prev_close;
    }


}
