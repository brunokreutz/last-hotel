package com.example.hotelBooking.service.exceptions;

public class ReservationTooFarException extends Exception {
    private int maxDaysBeforeBooking;

    public ReservationTooFarException(int maxDaysBeforeBooking) {
        super();
        this.maxDaysBeforeBooking = maxDaysBeforeBooking;
    }

    @Override
    public String getMessage() {
        return "Reservations can't be made " +
                maxDaysBeforeBooking +
                " days in advance";
    }
}
