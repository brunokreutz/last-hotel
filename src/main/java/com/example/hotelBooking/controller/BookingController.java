package com.example.hotelBooking.controller;


import com.example.hotelBooking.model.Booking;
import com.example.hotelBooking.model.BookingRequest;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.service.BookingService;
import com.example.hotelBooking.service.exceptions.ReservationMustBeAfterTodayException;
import com.example.hotelBooking.service.exceptions.ReservationTooFarException;
import com.example.hotelBooking.service.exceptions.BookingNotAvailableException;
import com.example.hotelBooking.service.exceptions.PeriodBiggerThanNDaysException;
import com.example.hotelBooking.service.exceptions.CheckoutDateInferiorThanCheckInDateException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @GetMapping
    public ResponseEntity<Iterable<Booking>> getAll(){
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/available-dates")
    public ResponseEntity<List<Date>> availableRooms(){
        return ResponseEntity.ok(bookingService.getAvailableRooms());
    }

    @GetMapping("{id}")
    public ResponseEntity<Booking> get(Long id){
        try {
            return new ResponseEntity<>(bookingService.find(id), HttpStatus.OK);
        }catch (NoSuchElementException ex){
            logger.info("Booking {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> bookRoom(@RequestBody @Valid BookingRequest bookingRequest) {
        try {
            return new ResponseEntity<>(bookingService.create(bookingRequest), HttpStatus.CREATED);
        } catch (PeriodBiggerThanNDaysException
                | CheckoutDateInferiorThanCheckInDateException
                | BookingNotAvailableException
                | ReservationTooFarException
                | ReservationMustBeAfterTodayException e) {
            logger.info("Not able to create booking, {}", e.getMessage());
            Map<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException ex){
            logger.info("Customer {} not found for booking.", bookingRequest.getCustomerId());
            Map<String, String> errors = new HashMap<>();
            errors.put("error","Customer not found for booking.");
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            logger.error("Unexpected error creating booking.",ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingRequest bookingRequest){
        try {
            Booking updatedBooking = bookingService.update(id, bookingRequest);
            if(updatedBooking == null){
                logger.info("Booking {} not found for update", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        }catch (NoSuchElementException ex){
            logger.info("Customer {} not found to update booking.", bookingRequest.getCustomerId());
            Map<String, String> errors = new HashMap<>();
            errors.put("error","Customer not found for booking.");
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            logger.error("Unexpected error updating booking {}.", id, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id){
        try {
            bookingService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoSuchElementException ex){
            logger.info("Booking {} not present for deletion", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception ex){
            logger.error("Unexpected error canceling booking.",ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
