package com.example.hotelBooking.controller;

import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.model.CustomerRequest;
import com.example.hotelBooking.service.CustomerService;
import com.example.hotelBooking.service.exceptions.EmailAlreadyUsedException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping
    public Iterable<Customer> getAll(){
        return customerService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> get(Long id){
        try {
            return new ResponseEntity<>(customerService.find(id), HttpStatus.OK);
        }catch (NoSuchElementException ex){
            logger.info("customer {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> post(@Valid @RequestBody CustomerRequest customerRequest){
        try {
            return new ResponseEntity<>(customerService.create(customerRequest), HttpStatus.CREATED);
        }catch (EmailAlreadyUsedException e) {
            logger.info("Failed to create customer, {}", customerRequest.getEmail());
            Map<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid Customer customer){
        try {
            Customer updatedCustomer = customerService.update(id, customer);
            if(updatedCustomer == null){
                logger.info("Customer {} not found for update", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        }catch (Exception ex){
            logger.error("Unexpected error updating customer {}.", id, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            customerService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoSuchElementException ex){
            logger.info("customer {} not present for deletion", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception ex){
            logger.error("Unexpected error canceling customer.",ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
