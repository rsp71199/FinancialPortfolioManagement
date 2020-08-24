package com.example.financialportfoliomanagement.Models;

import java.sql.Timestamp;

public class Chart {

    private Timestamp timestamp;
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Float volume;


    public Chart(Timestamp timestamp, Float open, Float high, Float low, Float close, Float volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}

