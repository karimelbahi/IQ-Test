package com.example.playmaker.onlinequizapplication.model;

/**
 * Created by playmaker on 3/29/2018.
 */

public class User {
    private String userName;
    private String password;
    //private String email;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
       // this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
