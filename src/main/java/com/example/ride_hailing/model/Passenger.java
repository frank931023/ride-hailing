package com.example.ride_hailing.model;

import java.util.UUID;

public class Passenger {

    private String id;
    private String name;
    private String phoneNumber;

    public Passenger(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public RideRequest requestRide(String pickUpLocation, String destination) {
        RideRequest rideRequest = new RideRequest(
            UUID.randomUUID().toString(),
            this,
            null,
            pickUpLocation,
            destination,
            "Pending"
        );
        System.out.println("Ride requested by passenger: " + name);
        return rideRequest;
    }

    public void cancelRide(RideRequest rideRequest) {
        if (!rideRequest.getPassenger().equals(this)) {
            System.out.println("This passenger is not associated with the ride.");
            return;
        }
        rideRequest.updateStatus("Canceled");
        System.out.println("Ride canceled by passenger: " + name);
    }

    public void viewDriverInfo(Driver driver) {
        System.out.println("Driver Info: Name - " + driver.getName() + ", Phone - " + driver.getPhoneNumber());
    }

    public void trackRideStatus(RideRequest rideRequest) {
        System.out.println("Ride Status: " + rideRequest.getStatus());
    }

    public void chooseDriver(Driver driver, RideRequest rideRequest) {
        if (!rideRequest.getPassenger().equals(this)) {
            System.out.println("This passenger is not associated with the ride.");
            return;
        }
        rideRequest.setDriver(driver);
        System.out.println("Driver chosen: " + driver.getName());
    }

}
