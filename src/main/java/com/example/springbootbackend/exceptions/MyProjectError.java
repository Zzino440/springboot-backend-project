package com.example.springbootbackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MyProjectError {

    //security errors
    TOKEN_EXPIRED("The token is expired", HttpStatus.UNAUTHORIZED),

    //authetication errors
    INVALID_CREDENTIALS("Both email and password are incorrect", HttpStatus.UNAUTHORIZED),
    EMAIL_NOT_FOUND("No user found with the provided email", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("The provided password is incorrect", HttpStatus.UNAUTHORIZED),


    //user errors
    USER_NOT_FOUND("User doesn't exist with the provided id", HttpStatus.NOT_FOUND),
    USERS_NOT_FOUND("No users found with the provided criteria", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_IN_USE("Email already in use", HttpStatus.NOT_ACCEPTABLE),
    USER_WITH_THIS_EMAIL_ALREADY_EXISTS("A user with this email already exists", HttpStatus.NOT_ACCEPTABLE),
    USER_CREATION_GENERIC_ERROR("An error occurred while creating the user", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String description;
    private final HttpStatus httpStatus;
}
