package com.sustech.flightbooking.domainmodel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Henry on 4/17/2017.
 */
public class Flight {
    private UUID id;
    private double price;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int capacity;

    public Flight(UUID id, double price, LocalDateTime departureTime, LocalDateTime arrivalTime, int capacity) {
        this.id = id;
        this.setPrice(price);
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.setCapacity(capacity);
    }

    public UUID getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
