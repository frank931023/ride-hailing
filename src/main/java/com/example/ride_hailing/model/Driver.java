package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private String id;
    private String name;
    private String phoneNumber;
    private String vehicleInfo;
    private boolean isAvailable;
    private List<Bid> submittedBids;

    public Driver(String id, String name, String phoneNumber, String vehicleInfo, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicleInfo = vehicleInfo;
        this.isAvailable = isAvailable;
        this.submittedBids = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<Bid> getSubmittedBids() {
        return submittedBids;
    }

    public void setAvailable(boolean newStatus) {
        this.isAvailable = newStatus;
        System.out.println("Driver " + name + " availability set to: " + (newStatus ? "Available" : "Unavailable"));
    }

    public Bid submitBid(RideRequest rideRequest, int price) {
        if (rideRequest.getStatus() != RequestStatus.INITIATE) {
            System.out.println("Cannot submit bid. RideRequest is not in INITIATE status.");
            return null;
        }
        
        Bid bid = new Bid(this.id, price);
        rideRequest.addBid(bid);
        this.submittedBids.add(bid);
        System.out.println("Driver " + name + " submitted bid of $" + price + " for ride " + rideRequest.getId());
        return bid;
    }

    public void withdrawBid(Bid bid, RideRequest rideRequest) {
        if (rideRequest.getStatus() != RequestStatus.INITIATE) {
            System.out.println("Cannot withdraw bid. RideRequest is not in INITIATE status.");
            return;
        }
        
        if (!this.submittedBids.contains(bid)) {
            System.out.println("This bid does not belong to driver " + name);
            return;
        }
        
        bid.withdraw();
        System.out.println("Driver " + name + " withdrew bid " + bid.getId());
    }

    public void notify(RideRequest rideRequest) {
        System.out.println("Driver " + name + " notified of new ride request: " + rideRequest.getId());
        System.out.println("  From: " + rideRequest.getPickUpLocation() + " To: " + rideRequest.getDestination());
        System.out.println("  Expected pickup time: " + rideRequest.getExpectedPickUpTime());
    }
}
