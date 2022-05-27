package com.example.hotelBooking.controller;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.example.hotelBooking.model.Customer;
import com.example.hotelBooking.model.CustomerRequest;
import com.example.hotelBooking.repository.CustomerRepository;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer = new Customer();

    @BeforeEach
    void createCustomer() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        customer.setEmail(person.getEmail());
        customer.setFirstName(person.getFirstName());
        customer.setLastName(person.getLastName());
        customerRepository.save(customer);
    }

    @Test
    void getAll() throws Exception {
        mvc.perform(get("/api/customer")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[-1].id").value(customer.getId()))
            .andExpect(jsonPath("$[-1].firstName").value(customer.getFirstName()))
            .andExpect(jsonPath("$[-1].lastName").value(customer.getLastName()))
            .andExpect(jsonPath("$[-1].email").value(customer.getEmail()));
    }

    @Test
    void createUser() throws Exception {
        Gson gson = new Gson();
        CustomerRequest customerRq = getCustomerRequest(customer);
        mvc.perform(post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(customerRq)))
            .andExpect(status().isCreated())
            .andExpect(content()
                    .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.firstName").value(customerRq.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(customerRq.getLastName()))
            .andExpect(jsonPath("$.email").value(customerRq.getEmail()));
    }

    private CustomerRequest getCustomerRequest(Customer customer) {
        return new CustomerRequest(customer.getFirstName(), customer.getLastName(), Fairy.create().person().getEmail());
    }

    @Test
    void updateUser() throws Exception {
        Gson gson = new Gson();
        mvc.perform(put("/api/customer/"+ customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(customer)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customer.getLastName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

    @Test
    void deleteUser() throws Exception{
        mvc.perform(delete("/api/customer/"+ customer.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}