package com.example.hotelBooking.service;

import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.model.CustomerRequest;
import com.example.hotelBooking.repository.CustomerRepository;
import com.example.hotelBooking.service.exceptions.EmailAlreadyUsedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Override
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer create(CustomerRequest customerRequest) throws EmailAlreadyUsedException {
        Customer customer = new Customer(customerRequest);
        if (customerRepository.findByEmail(customer.getEmail()) != null){
            throw new EmailAlreadyUsedException();
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        logger.info("Updating customer {}...", id);
        Customer updatedCustomer = customerRepository.findById(id).map(record -> {
            record.setLastName(customer.getLastName());
            record.setEmail(customer.getEmail());
            record.setFirstName(customer.getFirstName());
            return record;
        }).orElse(null);
        if (updatedCustomer == null){
            return null;
        }
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.findById(id).orElseThrow();
        logger.info("Deleting customer {}...", id);
        customerRepository.deleteById(id);
        logger.info("Customer {} deleted.", id);
    }

    @Override
    public Customer find(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }
}
