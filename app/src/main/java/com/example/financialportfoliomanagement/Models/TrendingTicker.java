package com.example.financialportfoliomanagement.Models;

public class TrendingTicker {
    public String shortName;
    public String regularMarketPrice;
    public String regularMarketChange;
    public String regularMarketChangePercent;
    public String quoteType;

    public TrendingTicker(String shortName, String regularMarketPrice, String regularMarketChange, String regularMarketChangePercent, String quoteType) {
        this.shortName = shortName;
        this.regularMarketPrice = regularMarketPrice;
        this.regularMarketChange = regularMarketChange + " (" + regularMarketChangePercent + ")";
        this.quoteType = quoteType;
    }
}
