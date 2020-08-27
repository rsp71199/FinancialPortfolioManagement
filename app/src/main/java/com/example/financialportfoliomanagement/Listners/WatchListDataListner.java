package com.example.financialportfoliomanagement.Listners;

import com.example.financialportfoliomanagement.Models.WatchListItem;

import java.util.List;

public interface WatchListDataListner {
    public void onDataFetched(List<WatchListItem> listItems);
}
