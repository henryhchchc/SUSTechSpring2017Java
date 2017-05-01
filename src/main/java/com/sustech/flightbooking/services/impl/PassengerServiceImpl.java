package com.sustech.flightbooking.services.impl;

import com.sustech.flightbooking.domainmodel.Passenger;
import com.sustech.flightbooking.persistence.PassengerRepository;
import com.sustech.flightbooking.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

}
