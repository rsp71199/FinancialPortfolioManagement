package com.example.financialportfoliomanagement.Models;

import java.util.List;

public class Question {
    public Question() {
    }

    public void setSelected_ans(String selected_ans) {
        this.selected_ans = selected_ans;
    }

    public String getSelected_ans() {
        return selected_ans;
    }

    private String selected_ans = "0";
    public String question;

    public List<String> getWeights() {
        return weights;
    }

    public void setWeights(List<String> weights) {
        this.weights = weights;
    }

    public List<String> weights;
    public List<String> options;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String type;

    public Question(String question, List<String> options, String type, List<String> weights) {
        this.question = question;
        this.type = type;
        this.selected_ans = "0";
        this.options = options;
        this.weights = weights;

    }
}
