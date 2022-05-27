package com.example.hotelBooking.service.exceptions;

public class EmailAlreadyUsedException extends Exception {
    @Override
    public String getMessage() {
        return "Email already used";
    }
}
