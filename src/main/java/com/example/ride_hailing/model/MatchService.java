package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;

public class MatchService {

    private List<RideRequest> rideRequests;
    private List<Driver> drivers;

    public MatchService(List<Driver> drivers) {
        this.drivers = drivers;
        this.rideRequests = new ArrayList<>();
    }

    public List<RideRequest> getRideRequests() {
        return rideRequests;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public Driver getDriverById(String driverId) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(driverId)) {
                return driver;
            }
        }
        return null;
    }

    // must
    public void createRideRequest(RideRequest rideRequest) {
        this.rideRequests.add(rideRequest);
        System.out.println("RideRequest " + rideRequest.getId() + " added to MatchService.");
        
        System.out.println("Broadcasting RideRequest " + rideRequest.getId() + " to all drivers...");
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                driver.notifyNewRide(rideRequest);
            }
        }
    }

    // must
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
        
        Driver driver = getDriverById(selectedBid.getDriver().getId());
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

    // must
    public RideRequest createRideRequest(Passenger passenger, String pickUpLocation, String destination, String expectedPickUpTime) {
        RideRequest rideRequest = new RideRequest(passenger, pickUpLocation, destination, expectedPickUpTime);
        System.out.println("Ride requested by passenger: " + passenger.getName());
        createRideRequest(rideRequest);
        return rideRequest;
    }

    public void selectBid(String bidId, RideRequest rideRequest) {
        for (Bid bid : rideRequest.getBids()) {
            if (bid.getId().equals(bidId)) {
                rideRequest.selectBid(bid);
                
                Driver driver = getDriverById(bid.getDriver().getId());
                if (driver != null) {
                    driver.setAvailable(false);
                }
                return;
            }
        }
    }

    // must
    public Bid submitBid(String driverId, int price, RideRequest rideRequest) {
        if (rideRequest.getStatus() != RequestStatus.INITIATE) {
            System.out.println("Cannot submit bid. RideRequest is not in INITIATE status.");
            return null;
        }
        
        Driver driver = getDriverById(driverId);
        if (driver == null) {
            System.out.println("Driver not found: " + driverId);
            return null;
        }

        Bid bid = new Bid(driver, price);
        rideRequest.addBid(bid);
        System.out.println("Driver " + driver.getName() + " submitted bid of $" + price + " for ride " + rideRequest.getId());
        return bid;
    }
}
