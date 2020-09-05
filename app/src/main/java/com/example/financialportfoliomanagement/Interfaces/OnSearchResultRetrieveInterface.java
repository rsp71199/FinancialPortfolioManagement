package com.example.financialportfoliomanagement.Interfaces;

import com.example.financialportfoliomanagement.Models.SearchResult;

import java.util.List;

public interface OnSearchResultRetrieveInterface {
    void onSearchResultRetrieveSuccess(List<SearchResult> searchResultList);
    void onSearchResultRetrieveFailure();
}
