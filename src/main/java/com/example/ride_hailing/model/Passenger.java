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
    public String showMatchSuccess(RideRequest rideRequest) {
        if (rideRequest.getStatus() != RequestStatus.MATCHED) {
            return "Cannot exchange contact info. Ride is not matched.";
        }

        Bid selectedBid = rideRequest.getSelectedBid();
        if (selectedBid == null) {
            return "No bid selected.";
        }
        Driver driver = selectedBid.getDriver();
        if (driver == null) {
            return "Driver not found.";
        }
        Passenger passenger = rideRequest.getPassenger();
        if (passenger == null) {
            return "Passenger not found.";
        }

        String info = "Match completed! Contact information exchanged:\n";
        info += "Passenger: " + passenger.getName() + " - " + passenger.getPhoneNumber() + "\n";
        info += "Driver: " + driver.getName() + " - " + driver.getPhoneNumber();
        System.out.println(info);
        return info;
    }
}
