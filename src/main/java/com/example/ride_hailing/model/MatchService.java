package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;

public class MatchService {

    // private RideRequest ridequest;
    private List<Driver> drivers;

    public MatchService(List<Driver> drivers) {
        this.drivers = drivers;
    }

    // send the request to all the drivers, and get the available driver list
    public List<Driver> askAvailableDriver(RideRequest rideRequest) {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        if (availableDrivers.isEmpty()) {
            System.out.println("No available drivers at the moment.");
        } else {
            System.out.println("Available drivers collected.");
        }
        return availableDrivers;
    }

    // send the collected accepted drivers to the passenger
    public void sendAcceptedDriver(List<Driver> drivers, RideRequest rideRequest) {
        rideRequest.updateStatus("Pending");
        System.out.println("Available drivers sent to passenger.");
        for (Driver driver : drivers) {
            System.out.println("Driver: " + driver.getName());
        }
    }

}
