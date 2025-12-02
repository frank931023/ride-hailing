package com.example.ride_hailing.model;

import java.util.UUID;

public class Bid {

    private String id;
    private String driverId;
    private int price;
    private BidStatus status;

    public Bid(String driverId, int price) {
        this.id = UUID.randomUUID().toString();
        this.driverId = driverId;
        this.price = price;
        this.status = BidStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getDriverId() {
        return driverId;
    }

    public int getPrice() {
        return price;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void withdraw() {
        if (this.status == BidStatus.PENDING) {
            this.status = BidStatus.WITHDRAWN;
            System.out.println("Bid " + id + " has been withdrawn by driver " + driverId);
        } else {
            System.out.println("Bid " + id + " cannot be withdrawn (status: " + status + ")");
        }
    }

    public boolean isPending() {
        return this.status == BidStatus.PENDING;
    }
}
