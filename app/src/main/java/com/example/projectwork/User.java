package com.example.projectwork;

public class User {
    public String username,email;

    public int xp,radius;

    public User(){

    }

    public User(String username, int xp, String email, int radius){
        this.username = username;
        this.xp = xp;
        this.email = email;
        this.radius=radius;
    }

    public void setRadius(int rad) {
        radius = rad;
    }

    public int getRadius() {
        return radius;
    }
}
