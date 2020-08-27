package com.example.financialportfoliomanagement.Models;

public class Index {
    public String shortName, regularMarketPrice, regularMarketChange, market_name;
    public boolean loss;

    public Index(String shortName, String regularMarketPrice, String regularMarketChange, String market_name, boolean loss) {
        this.shortName = shortName;
        this.loss = loss;
        this.regularMarketPrice = regularMarketPrice;
        this.regularMarketChange = regularMarketChange;
        this.market_name = market_name;
    }
}
