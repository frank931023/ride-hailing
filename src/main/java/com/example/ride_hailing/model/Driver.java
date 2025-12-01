package com.example.ride_hailing.model;

public class Driver {

    private String id;
    private String name;
    private String phoneNumber;
    private boolean isAvailable;

    public Driver(String id, String name, String phoneNumber, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean newStatus) {
        this.isAvailable = newStatus;
        System.out.println("Driver " + name + " availability set to: " + (newStatus ? "Available" : "Unavailable"));
    }

    // the driver confimr the ride, the status change into "matched"
    public boolean confirm(RideRequest rideRequest, boolean confirmRide) {
        if (!confirmRide) {
            System.out.println("Driver declined to confirm the ride.");
            rideRequest.updateStatus("Initiate");
            return false;
        }
        this.isAvailable = false;
        rideRequest.setDriver(this);
        rideRequest.updateStatus("Matched");
        System.out.println("Ride confirmed by driver: " + name);
        return true;
    }
}
