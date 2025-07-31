package com.abelichenko.models;

public class User {

    private int id;
    private String username;
    private int idRank;

    public User(int id, String username, int idRank) {
        this.id = id;
        this.username = username;
        this.idRank = idRank;
    }

    public User(String username, int idRank) {
        this.username = username;
        this.idRank = idRank;
    }

    public String getUsername() {
        return username;
    }

    public int getIdRank() {
        return idRank;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", idRank=" + idRank +
                '}';
    }
}