package com.example.hotelBooking.repository;

import com.example.hotelBooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCheckInBetweenOrderByCheckIn(Date checkInDate, Date checkOutDate);
}
