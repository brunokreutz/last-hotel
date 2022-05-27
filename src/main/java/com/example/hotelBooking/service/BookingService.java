package com.example.hotelBooking.service;

import com.example.hotelBooking.model.Booking;
import com.example.hotelBooking.model.BookingRequest;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.service.exceptions.*;

import java.sql.Date;
import java.util.List;

public interface BookingService {
    Iterable<Booking> getAll();

    Booking create(BookingRequest booking) throws PeriodBiggerThanNDaysException, CheckoutDateInferiorThanCheckInDateException, BookingNotAvailableException, ReservationTooFarException, ReservationMustBeAfterTodayException;

    List<Date> getAvailableRooms();

    Booking update(Long id, BookingRequest booking);

    void delete(Long id);

    Booking find(Long id);
}
