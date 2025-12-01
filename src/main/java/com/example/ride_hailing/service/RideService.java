package com.example.ride_hailing.service;

import com.example.ride_hailing.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    private MatchService matchService;
    private List<Driver> drivers;
    private RideRequest currentRideRequest;
    private Passenger currentPassenger;

    public RideService() {
        this.drivers = new ArrayList<>();
        // Initialize with 4 drivers as requested
        drivers.add(new Driver("1", "Driver 1", "1234567890", true));
        drivers.add(new Driver("2", "Driver 2", "0987654321", true));
        drivers.add(new Driver("3", "Driver 3", "1112223333", true));
        drivers.add(new Driver("4", "Driver 4", "4445556666", true));
        
        this.matchService = new MatchService(drivers);
        this.currentPassenger = new Passenger("p1", "Passenger 1", "5555555555");
    }

    public List<Driver> getAllDrivers() {
        return drivers;
    }

    public Driver getDriverById(String id) {
        return drivers.stream()
                .filter(d -> d.getName().equals(id) || d.getName().contains(id)) // Simplified matching for demo
                .findFirst()
                .orElse(null);
    }

    public RideRequest createRideRequest(String pickUpLocation, String destination) {
        currentRideRequest = new RideRequest();
        currentRideRequest.newRide(currentPassenger, pickUpLocation, destination);
        return currentRideRequest;
    }
    
    public void cancelRideRequest() {
        if (currentRideRequest != null) {
            currentPassenger.cancelRide(currentRideRequest);
            currentRideRequest = null; // Reset current request
        }
    }

    public RideRequest getCurrentRideRequest() {
        return currentRideRequest;
    }

    public List<Driver> getAvailableDrivers() {
        if (currentRideRequest == null) return new ArrayList<>();
        return matchService.askAvailableDriver(currentRideRequest);
    }

    public void passengerChooseDriver(String driverName) {
        if (currentRideRequest == null) return;
        
        Driver chosenDriver = getDriverById(driverName);
        if (chosenDriver != null) {
            // We create a list because the updated Passenger.chooseDriver expects a list
            List<Driver> driverList = new ArrayList<>();
            driverList.add(chosenDriver);
            currentPassenger.chooseDriver(driverList, currentRideRequest);
        }
    }

    public boolean driverConfirmRide(String driverName, boolean confirm) {
        if (currentRideRequest == null) return false;
        
        Driver driver = getDriverById(driverName);
        if (driver != null && currentRideRequest.getDriver() != null && 
            currentRideRequest.getDriver().getName().equals(driver.getName())) {
            return driver.confirm(currentRideRequest, confirm);
        }
        return false;
    }
    
    public void setDriverAvailability(String driverName, boolean available) {
        Driver driver = getDriverById(driverName);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }
}