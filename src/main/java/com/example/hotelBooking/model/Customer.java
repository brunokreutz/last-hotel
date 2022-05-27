package com.example.hotelBooking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    public Customer(CustomerRequest customerRequest){
        this.setEmail(customerRequest.getEmail());
        this.setFirstName(customerRequest.getFirstName());
        this.setLastName(customerRequest.getLastName());
    }
}
