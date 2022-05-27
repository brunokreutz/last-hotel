package com.example.hotelBooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
public class BookingRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private Date checkIn;
    @NotNull
    private Date checkOut;
}
