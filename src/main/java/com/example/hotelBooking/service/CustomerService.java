package com.example.hotelBooking.service;

import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.model.CustomerRequest;
import com.example.hotelBooking.service.exceptions.EmailAlreadyUsedException;

public interface CustomerService {
    Iterable<Customer> findAll();

    Customer create(CustomerRequest customerRequest) throws EmailAlreadyUsedException;

    Customer update(Long id, Customer customer);

    void delete(Long id);

    Customer find(Long id);
}
