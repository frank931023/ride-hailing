package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private String id;
    private String name;
    private String phoneNumber;
    private String vehicleInfo;
    private boolean isAvailable;

    public Driver(String id, String name, String phoneNumber, String vehicleInfo, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicleInfo = vehicleInfo;
        this.isAvailable = isAvailable;
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

    public void setAvailable(boolean newStatus) {
        this.isAvailable = newStatus;
        System.out.println("Driver " + name + " availability set to: " + (newStatus ? "Available" : "Unavailable"));
    }

    // must
    public void notifyNewRide(RideRequest rideRequest) {
        System.out.println("Driver " + name + " notified of new ride request: " + rideRequest.getId());
        System.out.println("  From: " + rideRequest.getPickUpLocation() + " To: " + rideRequest.getDestination());
        System.out.println("  Expected pickup time: " + rideRequest.getExpectedPickUpTime());
    }

    // must
    public void notifyMatchInfo(RideRequest rideRequest) {
        Passenger passenger = rideRequest.getPassenger();
        System.out.println("Driver " + name + " notified of match for ride: " + rideRequest.getId());
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Passenger phone number: " + passenger.getPhoneNumber());
        System.out.println("  Pick up: " + rideRequest.getPickUpLocation());
    }
}
