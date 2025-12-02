package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;

public class MatchService {

    private List<RideRequest> rideRequests;
    private List<Passenger> passengers;
    private List<Driver> drivers;

    public MatchService(List<Driver> drivers) {
        this.drivers = drivers;
        this.rideRequests = new ArrayList<>();
        this.passengers = new ArrayList<>();
    }

    public List<RideRequest> getRideRequests() {
        return rideRequests;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void addRideRequest(RideRequest rideRequest) {
        this.rideRequests.add(rideRequest);
        notifyAllDrivers(rideRequest);
        System.out.println("RideRequest " + rideRequest.getId() + " added to MatchService.");
    }

    public void notifyAllDrivers(RideRequest rideRequest) {
        System.out.println("Broadcasting RideRequest " + rideRequest.getId() + " to all drivers...");
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                driver.notify(rideRequest);
            }
        }
    }

    public Driver getDriverById(String driverId) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(driverId)) {
                return driver;
            }
        }
        return null;
    }

    public RideRequest getRideRequestById(String requestId) {
        for (RideRequest request : rideRequests) {
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;
    }

    public void cancelRideRequest(RideRequest rideRequest) {
        if (rideRequests.contains(rideRequest)) {
            rideRequest.cancel();
            System.out.println("RideRequest " + rideRequest.getId() + " cancelled in MatchService.");
        }
    }

    public String exchangeContactInfo(RideRequest rideRequest) {
        if (rideRequest.getStatus() != RequestStatus.MATCHED) {
            return "Cannot exchange contact info. Ride is not matched.";
        }
        
        Bid selectedBid = rideRequest.getSelectedBid();
        if (selectedBid == null) {
            return "No bid selected.";
        }
        
        Driver driver = getDriverById(selectedBid.getDriverId());
        if (driver == null) {
            return "Driver not found.";
        }
        
        Passenger passenger = rideRequest.getPassenger();
        
        String info = "Match completed! Contact information exchanged:\n";
        info += "Passenger: " + passenger.getName() + " - " + passenger.getPhoneNumber() + "\n";
        info += "Driver: " + driver.getName() + " - " + driver.getPhoneNumber();
        
        System.out.println(info);
        return info;
    }
}
