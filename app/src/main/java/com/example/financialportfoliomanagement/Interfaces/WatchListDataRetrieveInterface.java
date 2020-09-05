package com.example.financialportfoliomanagement.Interfaces;

import com.example.financialportfoliomanagement.Models.WatchListItem;

import java.util.List;

public interface WatchListDataRetrieveInterface {
    public void onDataFetched(List<WatchListItem> listItems);
}
