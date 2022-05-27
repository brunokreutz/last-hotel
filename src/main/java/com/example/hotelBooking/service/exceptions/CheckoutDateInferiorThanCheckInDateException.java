package com.example.hotelBooking.service.exceptions;

public class CheckoutDateInferiorThanCheckInDateException extends Exception {
    @Override
    public String getMessage() {
        return "Check out date cannot be inferior or equal check in date.";
    }
}
