package com.example.hotelBooking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Customer customer;
    @NotNull
    private Date checkIn;
    @NotNull
    private Date checkOut;

    public Booking(Customer customer, BookingRequest bookingRequest) {
        this.setCustomer(customer);
        this.setCheckIn(bookingRequest.getCheckIn());
        this.setCheckOut(bookingRequest.getCheckOut());
    }
}
