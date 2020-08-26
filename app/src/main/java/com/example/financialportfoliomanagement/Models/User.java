package com.example.financialportfoliomanagement.Models;

public class User {
    public User() {
    }

    public User(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    private String name;
    private String user_id;


    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }


}
