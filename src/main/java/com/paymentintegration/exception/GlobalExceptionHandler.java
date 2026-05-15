package com.paymentintegration.exception;

import com.paymentintegration.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        String errorMessage =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();


        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage
                );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleResourceNotFoundException(ResourceNotFoundException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND
        );
    }
}