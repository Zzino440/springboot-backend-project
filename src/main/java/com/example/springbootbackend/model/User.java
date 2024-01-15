package com.example.springbootbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email_id")
    private String emailID;

    public User() {

    }

    public User(String firstName, String lastName, String emailID) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
    }

}
