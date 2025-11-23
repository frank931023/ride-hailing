package com.example.ride_hailing.model;

import java.util.List;

public class MatchService {

    private List<Driver> drivers;

    public MatchService(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Driver matchDriver(RideRequest rideRequest) {
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                driver.acceptRide(rideRequest);
                System.out.println("Driver matched: " + driver.getName());
                return driver;
            }
        }
        System.out.println("No available drivers at the moment.");
        return null;
    }

    public void notifyPassenger(RideRequest rideRequest) {
        System.out.println("Notification sent to passenger: Ride status - " + rideRequest.getStatus());
        alert("Notification sent to passenger: Ride status - " + rideRequest.getStatus());
    }

    public void notifyDriver(RideRequest rideRequest) {
        System.out.println("Notification sent to driver: Ride status - " + rideRequest.getStatus());
        alert("Notification sent to driver: Ride status - " + rideRequest.getStatus())
    }

}
