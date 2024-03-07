package com.example.springbootbackend.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        // Creazione di un messaggio di errore personalizzato
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        // Restituisci una risposta con lo stato BAD_REQUEST e il messaggio di errore
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        // Creazione di un messaggio di errore personalizzato
        String errorMessage = ex.getReason();

        // Restituisci una risposta con lo stato definito nell'eccezione e il messaggio di errore
        return ResponseEntity.status(ex.getStatusCode()).body(errorMessage);
    }

    /**
     * Custom Exception Handler per questo progetto*/
    @ExceptionHandler(value = MyProjectException.class)
    @ResponseBody
    public ResponseEntity<Object> handleCustomExceptions(MyProjectException myProjectException) {

        //utilizzo il metodo getEffectiveDescription() per recuperare l'errore in modo dinamico
        String errorMessage = myProjectException.getEffectiveDescription();
        HttpStatus errorCode = myProjectException.getMyProjectError().getHttpStatus();

        return ResponseEntity.status(errorCode).body(errorMessage);
    }


    // Altri gestori di eccezioni...
}
