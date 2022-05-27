package com.example.hotelBooking.service.exceptions;

public class BookingNotAvailableException extends Exception {
    @Override
    public String getMessage() {
        return "Date is already booked";
    }
}
