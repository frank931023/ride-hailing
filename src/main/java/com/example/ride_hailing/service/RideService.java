package com.example.ride_hailing.service;

import com.example.ride_hailing.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideService {

    private MatchService matchService;
    private List<Driver> drivers;
    private RideRequest currentRideRequest;
    private Passenger currentPassenger;

    public RideService() {
        this.drivers = new ArrayList<>();
        // Initialize with 4 drivers with vehicle info
        drivers.add(new Driver("1", "Driver 1", "1234567890", "Toyota Camry - ABC123", true));
        drivers.add(new Driver("2", "Driver 2", "0987654321", "Honda Civic - XYZ789", true));
        drivers.add(new Driver("3", "Driver 3", "1112223333", "Tesla Model 3 - TES456", true));
        drivers.add(new Driver("4", "Driver 4", "4445556666", "BMW 3 Series - BMW999", true));
        
        this.matchService = new MatchService(drivers);
        this.currentPassenger = new Passenger("p1", "Passenger 1", "5555555555");
    }

    public List<Driver> getAllDrivers() {
        return drivers;
    }

    public Driver getDriverById(String id) {
        return matchService.getDriverById(id);
    }

    public RideRequest createRideRequest(String pickUpLocation, String destination, String expectedPickUpTime) {
        currentRideRequest = currentPassenger.createRequest(pickUpLocation, destination, expectedPickUpTime);
        matchService.addRideRequest(currentRideRequest);
        return currentRideRequest;
    }
    
    public void cancelRideRequest() {
        if (currentRideRequest != null) {
            currentPassenger.cancelRide(currentRideRequest);
            currentRideRequest = null;  // 清空當前請求
        }
    }

    public RideRequest getCurrentRideRequest() {
        return currentRideRequest;
    }

    public List<Bid> getPendingBids() {
        if (currentRideRequest == null) return new ArrayList<>();
        return currentRideRequest.getPendingBids();
    }

    public List<Bid> getAllBids() {
        if (currentRideRequest == null) return new ArrayList<>();
        return currentRideRequest.getBids();
    }

    public Bid submitBid(String driverId, int price) {
        if (currentRideRequest == null) {
            System.out.println("No active ride request.");
            return null;
        }
        
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            return driver.submitBid(currentRideRequest, price);
        }
        return null;
    }

    public void withdrawBid(String bidId, String driverId) {
        if (currentRideRequest == null) return;
        
        Driver driver = getDriverById(driverId);
        if (driver == null) return;
        
        // Find the bid
        for (Bid bid : currentRideRequest.getBids()) {
            if (bid.getId().equals(bidId)) {
                driver.withdrawBid(bid, currentRideRequest);
                return;
            }
        }
    }

    public void passengerSelectBid(String bidId) {
        if (currentRideRequest == null) return;
        
        // Find the bid
        for (Bid bid : currentRideRequest.getBids()) {
            if (bid.getId().equals(bidId)) {
                currentPassenger.selectBid(bid, currentRideRequest);
                
                // Mark driver as unavailable
                Driver driver = getDriverById(bid.getDriverId());
                if (driver != null) {
                    driver.setAvailable(false);
                }
                return;
            }
        }
    }

    public String getMatchedContactInfo() {
        if (currentRideRequest == null) return "No active ride request.";
        return matchService.exchangeContactInfo(currentRideRequest);
    }

    public void setDriverAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }
}