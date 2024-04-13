package com.example.springbootbackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MyProjectError {

    EMAIL_ALREADY_IN_USE("Email already in use", HttpStatus.NOT_ACCEPTABLE),
    USER_WITH_THIS_EMAIL_ALREADY_EXISTS("A user with this email already exists", HttpStatus.NOT_ACCEPTABLE);

    private final String description;
    private final HttpStatus httpStatus;
}
