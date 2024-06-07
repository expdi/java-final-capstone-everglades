package com.expeditors.trackservice.exception;

import com.expeditors.trackservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    private ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();

        ProblemDetail validationProblemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Error");

        var errorResponse = ErrorResponse.builder()
                .message("Validation Error")
                .errors(errors
                        .stream()
                        .map(error -> generateErrorMessage(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
                        .toList())
                .build();

        validationProblemDetail.setProperty("errors", errorResponse);
        return validationProblemDetail;
    }

    private String generateErrorMessage(String defaultMessage, String field, Object rejectedValue) {

        StringBuilder builder = new StringBuilder();
        builder.append(field);
        builder.append(":");
        builder.append(defaultMessage);
        builder.append(", supplied Value: ");
        builder.append(Objects.isNull(rejectedValue) ?"null" :rejectedValue.toString());
        return builder.toString();
    }

    @ExceptionHandler(value = {Exception.class})
    private ProblemDetail lastPortOfCall(Exception ex) {

        ProblemDetail unexpectedExceptionProblemDetail  =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Validation Error");

        unexpectedExceptionProblemDetail.setProperty(
                "ExceptionError",ex.getMessage());

        return unexpectedExceptionProblemDetail;
    }


}
