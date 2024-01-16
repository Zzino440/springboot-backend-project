package com.example.springbootbackend.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {


    private Long id;
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;

    public UserDTO(Long id, String firstName, String lastName, String emailID, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.password = password;
    }

    public UserDTO() {
    }
}
