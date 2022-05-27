package com.example.hotelBooking.controller;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.example.hotelBooking.model.Booking;
import com.example.hotelBooking.model.BookingRequest;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.model.CustomerRequest;
import com.example.hotelBooking.repository.BookingRepository;
import com.example.hotelBooking.repository.CustomerRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private static Customer customer = new Customer();
    Booking booking = new Booking();

    @BeforeEach
    void setUp() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        customer.setEmail(person.getEmail());
        customer.setFirstName(person.getFirstName());
        customer.setLastName(person.getLastName());
        customer = customerRepository.save(customer);
        booking.setCheckIn(Date.valueOf(LocalDate.now().plusDays(1)));
        booking.setCheckOut(Date.valueOf(LocalDate.now().plusDays(2)));
        booking.setCustomer(customer);
        bookingRepository.save(booking);
    }


    @Test
    void getAll() throws Exception {
        mvc.perform(get("/api/booking")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].checkIn").exists())
            .andExpect(jsonPath("$[0].checkOut").exists())
            .andExpect(jsonPath("$[0].customer").exists());
    }

    @Test
    void availableRooms() throws Exception {
        mvc.perform(get("/api/booking/available-dates")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0]").value(Date.valueOf(LocalDate.now().plusDays(3)).toString()));

    }

    @Test
    void bookRoom() throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckIn(Date.valueOf(LocalDate.now().plusDays(9)));
        bookingRequest.setCheckOut(Date.valueOf(LocalDate.now().plusDays(10)));
        bookingRequest.setCustomerId(1L);
        mvc.perform(post("/api/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.checkIn").value(Date.valueOf(LocalDate.now().plusDays(9)).toString()))
                .andExpect(jsonPath("$.checkOut").value(Date.valueOf(LocalDate.now().plusDays(10)).toString()))
                .andExpect(jsonPath("$.customer").value(customer));
    }

    @Test
    void updateBooking() throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckIn(Date.valueOf(LocalDate.now().plusDays(9)));
        bookingRequest.setCheckOut(Date.valueOf(LocalDate.now().plusDays(10)));
        bookingRequest.setCustomerId(1L);
        mvc.perform(put("/api/booking/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.checkIn").value(Date.valueOf(LocalDate.now().plusDays(9)).toString()))
                .andExpect(jsonPath("$.checkOut").value(Date.valueOf(LocalDate.now().plusDays(10)).toString()))
                .andExpect(jsonPath("$.customer").value(customer));
    }

    @Test
    void cancelBooking() throws Exception {
        mvc.perform(delete("/api/booking/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}