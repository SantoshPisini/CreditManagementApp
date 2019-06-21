package com.santosh.creditmanagementapp;

public class User {
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_phone;
    private String user_credits;

    public User(String user_id, String user_name, String user_email, String user_phone,String user_credits) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_credits = user_credits;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_credits() {
        return user_credits;
    }
}
