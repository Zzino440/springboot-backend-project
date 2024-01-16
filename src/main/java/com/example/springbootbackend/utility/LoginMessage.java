package com.example.springbootbackend.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMessage {
    String message;
    Boolean status;
    public LoginMessage(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }
}
