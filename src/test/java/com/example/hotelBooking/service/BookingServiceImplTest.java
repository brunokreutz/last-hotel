package com.example.hotelBooking.service;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.example.hotelBooking.model.Booking;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.repository.BookingRepository;
import com.example.hotelBooking.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class BookingServiceImplTest {
    private BookingRepository bookingRepository = mock(BookingRepository.class);
    private CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(bookingRepository, customerRepository);
    private final Fairy fairy = Fairy.create();

    @Test
    void getAvailableRooms() {
        List<Booking> bookingList = new ArrayList<>();
        Customer customer = getCustomer();
        Booking b1 = getBooking(customer, 2,5);
        bookingList.add(b1);
        Booking b2 = getBooking(customer, 10,11);
        bookingList.add(b2);

        when(bookingRepository.findByCheckInBetweenOrderByCheckIn(any(), any())).thenReturn(bookingList);

        List<Date> availableRooms = bookingServiceImpl.getAvailableRooms();
        Assertions.assertNotNull(availableRooms);
    }

    private Booking getBooking(Customer customer, int checkIn, int checkOut) {
        Booking b1 = new Booking();
        b1.setCustomer(customer);
        b1.setCheckIn(Date.valueOf(LocalDate.now().plusDays(checkIn)));
        b1.setCheckOut(Date.valueOf(LocalDate.now().plusDays(checkOut)));
        return b1;
    }

    private Customer getCustomer() {
        Person person = fairy.person();
        Customer customer = new Customer();
        customer.setEmail(person.getEmail());
        customer.setFirstName(person.getFirstName());
        customer.setLastName(person.getLastName());
        return customer;
    }
}