package com.example.financialportfoliomanagement.Interfaces;

import com.example.financialportfoliomanagement.Models.News;

import java.util.List;

public interface OnNewsRetrieveInterface {
    void success(List<News> newsList);

    void failure(Exception e);
}
