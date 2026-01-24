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
        // get Driver
        Driver driver = rideRequest.getSelectedBid().getDriver();
        Passenger passenger = rideRequest.getPassenger();
        // 叫passenger 方法
        String info = passenger.showMatchSuccess(rideRequest);
        // 叫driver 方法
        driver.notifyMatchInfo(rideRequest);
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
    public Bid submitBid(Driver driver, int price, RideRequest rideRequest) {
        if (rideRequest.getStatus() != RequestStatus.INITIATE) {
            System.out.println("Cannot submit bid. RideRequest is not in INITIATE status.");
            return null;
        }
        
        if (driver == null) {
            System.out.println("Driver not found");
            return null;
        }

        Bid bid = new Bid(driver, price);
        rideRequest.addBid(bid);
        System.out.println("Driver " + driver.getName() + " submitted bid of $" + price + " for ride " + rideRequest.getId());
        return bid;
    }
}
