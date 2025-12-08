package com.example.ride_hailing.model;

import java.util.ArrayList;
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

    // must
    public List<Bid> updateBideList(RideRequest currentRideRequest) {
        if (currentRideRequest != null) {
            return currentRideRequest.getBids();
        }
        return new ArrayList<>();
    }

    // must
    public void showMatchSucess(RideRequest currentRideRequest) {
        if (currentRideRequest != null && currentRideRequest.getStatus() == RequestStatus.MATCHED) {
            System.out.println("Match Success! Ride " + currentRideRequest.getId() + " is matched.");
        } else {
            System.out.println("No successful match yet.");
        }
    }
}
