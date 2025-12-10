package com.example.ride_hailing.service;

import com.example.ride_hailing.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideService {

    private final MatchService matchService;
    private final List<Driver> drivers;
    private final Passenger currentPassenger;
    private RideRequest currentRideRequest;

    public RideService() {
        this.drivers = initializeDrivers();
        this.matchService = new MatchService(drivers);
        this.currentPassenger = new Passenger("p1", "Passenger 1", "5555555555");
    }

    private List<Driver> initializeDrivers() {
        List<Driver> driverList = new ArrayList<>();
        driverList.add(new Driver("1", "Wilson Wong", "1234567890", "Toyota Camry - ABC123", true));
        driverList.add(new Driver("2", "Loyuyu Fu", "0987654321", "Honda Civic - XYZ789", true));
        driverList.add(new Driver("3", "Jack Hung", "1112223333", "Tesla Model 3 - TES456", true));
        driverList.add(new Driver("4", "Oswin Q", "4445556666", "BMW 3 Series - BMW999", true));
        return driverList;
    }

    public List<Driver> getAllDrivers() {
        return drivers;
    }

    public Driver getDriverById(String id) {
        return matchService.getDriverById(id);
    }

    public RideRequest createRideRequest(String pickUpLocation, String destination, String expectedPickUpTime) {
        currentRideRequest = matchService.createRideRequest(currentPassenger, pickUpLocation, destination, expectedPickUpTime);
        return currentRideRequest;
    }
    
    public void cancelRideRequest() {
        if (currentRideRequest != null) {
            matchService.cancelRideRequest(currentRideRequest);
            currentRideRequest = null;  // 清空當前請求
        }
    }

    public RideRequest getCurrentRideRequest() {
        return currentRideRequest;
    }

    public List<Bid> getAllBids() {
        return currentPassenger.updateBideList(currentRideRequest);
    }

    public Bid submitBid(Driver driver, int price) {
        if (currentRideRequest == null) {
            System.out.println("No active ride request.");
            return null;
        }
        
        return matchService.submitBid(driver, price, currentRideRequest);
    }

    public void passengerSelectBid(String bidId) {
        if (currentRideRequest == null) return;
        matchService.selectBid(bidId, currentRideRequest);
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