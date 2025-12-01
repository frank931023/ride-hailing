package com.example.ride_hailing.model;

import java.util.List;
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

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RideRequest requestRide(String pickUpLocation, String destination) {
        RideRequest rideRequest = new RideRequest();
        rideRequest.newRide(this, pickUpLocation, destination);
        System.out.println("Ride requested by passenger: " + name);
        return rideRequest;
    }

    // the passenger cancel the ride "actively"
    public void cancelRide(RideRequest rideRequest) {
        // if (!rideRequest.getPassenger().equals(this)) {
        //     System.out.println("This passenger is not associated with the ride.");
        //     return;
        // }
        rideRequest.updateStatus("Cancelled");
        System.out.println("Ride canceled by passenger: " + name);
    }

    public void chooseDriver(List<Driver> availableDrivers, RideRequest rideRequest) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            System.out.println("No drivers available to choose from.");
            return;
        }

        // For simplicity, choose the first available driver
        Driver chosenDriver = availableDrivers.get(0);
        if (chosenDriver.isAvailable()) {
            rideRequest.setDriver(chosenDriver);
            System.out.println("Driver chosen: " + chosenDriver.getName());
        } else {
            System.out.println("Driver is no longer available.");
        }
    }

}
