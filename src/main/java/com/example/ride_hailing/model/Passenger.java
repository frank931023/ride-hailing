package com.example.ride_hailing.model;

import java.util.List;

public class Passenger {

    private String id;
    private String name;
    private String phoneNumber;

    public Passenger(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public RideRequest createRequest(String pickUpLocation, String destination, String expectedPickUpTime) {
        RideRequest rideRequest = new RideRequest(this, pickUpLocation, destination, expectedPickUpTime);
        System.out.println("Ride requested by passenger: " + name);
        return rideRequest;
    }

    public void cancelRide(RideRequest rideRequest) {
        if (rideRequest.getPassenger() != this) {
            System.out.println("This passenger is not associated with the ride.");
            return;
        }
        rideRequest.cancel();
        System.out.println("Ride canceled by passenger: " + name);
    }

    public void selectBid(Bid bid, RideRequest rideRequest) {
        if (rideRequest.getPassenger() != this) {
            System.out.println("This passenger is not associated with the ride.");
            return;
        }
        
        rideRequest.selectBid(bid);
        System.out.println("Passenger " + name + " selected bid: " + bid.getId());
    }
}
