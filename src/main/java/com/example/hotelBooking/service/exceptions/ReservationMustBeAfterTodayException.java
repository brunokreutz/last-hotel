package com.example.hotelBooking.service.exceptions;

public class ReservationMustBeAfterTodayException extends Exception {
    @Override
    public String getMessage() {
        return "Reservations must start from tomorrow onwards.";
    }
}
