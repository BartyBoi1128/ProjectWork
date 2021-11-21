package com.example.projectwork;

public class User {
    public String username,email;
    public int xp;

    public User(){

    }

    public User(String username, int xp, String email){
        this.username = username;
        this.xp = xp;
        this.email = email;
    }
}
