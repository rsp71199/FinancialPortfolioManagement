package com.example.financialportfoliomanagement.Interfaces;

public interface OnCandleChartsDataRetrieveInterface {
    void onCandleChartDataRetrieveSuccess(String res,String arrayKey);
    void onCandleChartDataRetrieveFailure();
}
