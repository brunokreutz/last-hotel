package com.example.hotelBooking.repository;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.example.hotelBooking.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        createCustomer();
    }

    private void createCustomer() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        Customer customer = new Customer();
        customer.setEmail(person.getEmail());
        customer.setFirstName(person.getFirstName());
        customer.setLastName(person.getLastName());
        customerRepository.save(customer);
    }

    @Test
    void insertNewCustomer(){
        createCustomer();
        Iterable<Customer> all = customerRepository.findAll();
        all.forEach( customer ->
                assertNotNull(customer.getEmail()));
    }

}