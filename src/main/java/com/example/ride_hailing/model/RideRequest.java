package com.example.ride_hailing.model;

public class RideRequest {

    private String id;
    private Passenger passenger;
    private Driver driver;
    private String pickUpLocation;
    private String destination;
    private String status;

    public RideRequest(
        String id, 
        Passenger passenger, 
        Driver driver, 
        String pickUpLocation, 
        String destination, 
        String status
    ) {
        this.id = id;
        this.passenger = passenger;
        this.driver = driver;
        this.pickUpLocation = pickUpLocation;
        this.destination = destination;
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.isEmpty()) {
            System.out.println("Invalid status.");
            return;
        }
        this.status = newStatus;
        System.out.println("Ride status updated to: " + status);
    }

    public void setDriver(Driver driver) {
        if (driver == null) {
            System.out.println("Invalid driver.");
            return;
        }
        this.driver = driver;
        System.out.println("Driver assigned: " + driver.getName());
    }

    public String getStatus() {
        return status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestination() {
        return destination;
    }
}
