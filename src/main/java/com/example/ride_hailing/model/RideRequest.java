package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideRequest {

    private String id;
    private Passenger passenger;
    private String pickUpLocation;
    private String destination;
    private String expectedPickUpTime;
    private RequestStatus status;
    private List<Bid> bids;
    private Bid selectedBid;

    public RideRequest() {
        // Default constructor for creating an empty RideRequest instance
        this.bids = new ArrayList<>();
    }

    public RideRequest(Passenger passenger, String pickUpLocation, String destination, String expectedPickUpTime) {
        this.id = UUID.randomUUID().toString();
        this.passenger = passenger;
        this.pickUpLocation = pickUpLocation;
        this.destination = destination;
        this.expectedPickUpTime = expectedPickUpTime;
        this.status = RequestStatus.INITIATE;
        this.bids = new ArrayList<>();
        this.selectedBid = null;
        System.out.println("New ride created: " + id);
    }

    public String getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getExpectedPickUpTime() {
        return expectedPickUpTime;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public Bid getSelectedBid() {
        return selectedBid;
    }

    // must
    public void addBid(Bid bid) {
        if (this.status != RequestStatus.INITIATE) {
            System.out.println("Cannot add bid. RideRequest status is not INITIATE.");
            return;
        }
        this.bids.add(bid);
        System.out.println("Bid added to RideRequest " + id + " from driver " + bid.getDriver().getId());
    }

    // must
    public void selectBid(Bid bid) {
        if (this.status != RequestStatus.INITIATE) {
            System.out.println("Cannot select bid. RideRequest is not in INITIATE status.");
            return;
        }
        if (!bids.contains(bid)) {
            System.out.println("Bid not found in this request.");
            return;
        }
        this.selectedBid = bid;
        this.status = RequestStatus.MATCHED;
        bid.setAccepted(true);
        System.out.println("Bid " + bid.getId() + " selected. Ride matched.");
    }

    public void cancel() {
        if (this.status == RequestStatus.INITIATE) {
            this.status = RequestStatus.CANCELLED;
            System.out.println("RideRequest " + id + " has been cancelled.");
        } else {
            System.out.println("Cannot cancel RideRequest. Current status: " + status);
        }
    }
}
