package com.example.hotelBooking.service.exceptions;

public class PeriodBiggerThanNDaysException extends Exception {
    private int maxStay;

    public PeriodBiggerThanNDaysException(int maxStay) {
        super();
        this.maxStay = maxStay;
    }

    @Override
    public String getMessage() {
        return "Stay cannot be longer than 3 days.";
    }
}
