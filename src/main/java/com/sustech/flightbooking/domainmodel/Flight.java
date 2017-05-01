package com.sustech.flightbooking.domainmodel;

import org.mongodb.morphia.annotations.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Flight extends EntityBase {

    private double price;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int capacity;

    public Flight(UUID id, double price, LocalDateTime departureTime, LocalDateTime arrivalTime, int capacity) {
        super(id);
        this.setPrice(price);
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.setCapacity(capacity);
    }

    public Flight() {
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
