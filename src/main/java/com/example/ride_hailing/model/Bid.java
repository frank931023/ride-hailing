package com.example.ride_hailing.model;

import java.util.UUID;

public class Bid {

    private String id;
    private Driver driver;
    private int price;
    private boolean accepted;

    public Bid(Driver driver, int price) {
        this.id = UUID.randomUUID().toString();
        this.driver = driver;
        this.price = price;
        this.accepted = false;
    }

    public String getId() {
        return id;
    }

    public Driver getDriver() {
        return driver;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isPending() {
        return !accepted;
    }
}
