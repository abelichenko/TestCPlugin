package com.abelichenko.models;

public class Rank {

    private int id;
    private String name;
    private int perms;

    public Rank(int id, String name, int perms) {
        this.id = id;
        this.name = name;
        this.perms = perms;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPerms() {
        return perms;
    }

    public void setPerms(int perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", perms=" + perms +
                '}';
    }
}