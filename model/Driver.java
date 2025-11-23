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

    public boolean acceptRide(RideRequest rideRequest) {
        if (!isAvailable) {
            System.out.println("Driver is not available.");
            return false;
        }
        this.isAvailable = false;
        rideRequest.setDriver(this);
        rideRequest.updateStatus("Matched");
        System.out.println("Ride accepted by driver: " + name);
        return true;
    }

    public void rejectRide(RideRequest rideRequest) {
        System.out.println("Ride rejected by driver: " + name);
        rideRequest.updateStatus("Canceled");
    }

    public void startTrip(RideRequest rideRequest) {
        if (rideRequest.getDriver() != this) {
            System.out.println("This driver is not assigned to the ride.");
            return;
        }
        rideRequest.updateStatus("In Progress");
        System.out.println("Trip started by driver: " + name);
    }

    public void completeTrip(RideRequest rideRequest) {
        if (rideRequest.getDriver() != this) {
            System.out.println("This driver is not assigned to the ride.");
            return;
        }
        rideRequest.updateStatus("Completed");
        this.isAvailable = true;
        System.out.println("Trip completed by driver: " + name);
    }

}
