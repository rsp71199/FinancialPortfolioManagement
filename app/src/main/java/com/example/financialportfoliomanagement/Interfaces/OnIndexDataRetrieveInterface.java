package com.example.financialportfoliomanagement.Interfaces;

import com.example.financialportfoliomanagement.Models.Index;

import java.util.List;

public interface OnIndexDataRetrieveInterface {
    void onIndexDataRetrieveSuccess(List<Index> indexList);
    void onIndexDataRetrieveFailure();
}
