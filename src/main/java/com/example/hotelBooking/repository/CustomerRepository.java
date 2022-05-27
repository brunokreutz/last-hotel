package com.example.hotelBooking.repository;

import com.example.hotelBooking.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByEmail(String email);
}
