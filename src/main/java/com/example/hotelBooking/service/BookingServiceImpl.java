package com.example.hotelBooking.service;

import com.example.hotelBooking.model.Booking;
import com.example.hotelBooking.model.BookingRequest;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.repository.BookingRepository;
import com.example.hotelBooking.repository.CustomerRepository;
import com.example.hotelBooking.service.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Value("${app.maxDaysBeforeBooking}")
    private  int maxDaysBeforeBooking;
    @Value("${app.maxStay}")
    private  int maxStay;

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Override
    public Iterable<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking create(BookingRequest bookingRequest) throws PeriodBiggerThanNDaysException, CheckoutDateInferiorThanCheckInDateException, BookingNotAvailableException, ReservationTooFarException, ReservationMustBeAfterTodayException {
        logger.info("Creating new booking...");
        long period = ChronoUnit.DAYS.between(bookingRequest.getCheckIn().toLocalDate(), bookingRequest.getCheckOut().toLocalDate());
        if(period > maxStay ){
            throw new PeriodBiggerThanNDaysException(maxStay);
        }
        if(period < 1){
            throw new CheckoutDateInferiorThanCheckInDateException();
        }
        if(ChronoUnit.DAYS.between(LocalDate.now(), bookingRequest.getCheckIn().toLocalDate()) > maxDaysBeforeBooking){
            throw new ReservationTooFarException(maxDaysBeforeBooking);
        }
        if(!LocalDate.now().isBefore(bookingRequest.getCheckIn().toLocalDate())){
            throw new ReservationMustBeAfterTodayException();
        }

        List<Booking> currentBookings = bookingRepository.findByCheckInBetweenOrderByCheckIn(bookingRequest.getCheckIn(), bookingRequest.getCheckOut());
        if(!currentBookings.isEmpty()){
            throw new BookingNotAvailableException();
        }

        Customer customer = customerRepository.findById(bookingRequest.getCustomerId()).orElseThrow();
        Booking booking = new Booking(customer, bookingRequest);

        bookingRepository.save(booking);
        logger.info("New booking created successfully!");
        return booking;
    }

    @Override
    /*
      SINCE THE GUESTS CAN'T MAKE A RESERVATION 30 DAYS IN ADVANCE WE WILL ONLY SHOW THE AVAILABLE DATES IN THE NEXT 30 DAYS
     */
    public List<Date> getAvailableRooms() {
        LocalDate today = LocalDate.now();
        logger.info("Getting current bookings...");
        List<Booking> currentBookings = bookingRepository.findByCheckInBetweenOrderByCheckIn(Date.valueOf(today), Date.valueOf(today.plusDays(maxDaysBeforeBooking)));
        logger.info("{} bookings found.",currentBookings.size());
        List<Date> availableDates = new ArrayList<>();
        int i =0;
        //this nested loops are actually O(n)
        //start from 1 because we don't show today as available for booking
        logger.info("Calculating available dates...");
        for (int j = 1; j < maxDaysBeforeBooking; j++){
            Date date = Date.valueOf(LocalDate.now().plusDays(j));
            if(currentBookings.size() > i && currentBookings.get(i).getCheckIn().equals(date)) {
                //increment j until we find the last day of booking
                while (currentBookings.get(i).getCheckOut().after(Date.valueOf(LocalDate.now().plusDays(j))) && j < maxDaysBeforeBooking) {
                    j++;
                }
                //increment i when we are done with this currentBooking
                i++;
            }
            else {
                availableDates.add(date);
            }
        }
        logger.info("Available dates calculation finished.");
        return availableDates;
    }

    @Override
    public Booking update(Long id, BookingRequest bookingRequest) {
        logger.info("Validating if customer exists {}...", bookingRequest.getCustomerId());
        Customer customer = customerRepository.findById(bookingRequest.getCustomerId()).orElseThrow();
        Booking booking = new Booking(customer, bookingRequest);
        logger.info("Updating booking {}...", id);
        Booking updatedBooking = bookingRepository.findById(id).map(record -> {
            record.setCustomer(booking.getCustomer());
            record.setCheckOut(booking.getCheckOut());
            record.setCheckIn(booking.getCheckIn());
            return record;
        }).orElse(null);
        if (updatedBooking == null){
            return null;
        }
        return bookingRepository.save(updatedBooking);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.findById(id).orElseThrow();
        logger.info("Deleting booking {}...", id);
        bookingRepository.deleteById(id);
        logger.info("Booking {} deleted.", id);
    }

    @Override
    public Booking find(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }
}

