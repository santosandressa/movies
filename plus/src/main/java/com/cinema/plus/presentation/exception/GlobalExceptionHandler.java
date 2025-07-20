package com.cinema.plus.presentation.exception;

import com.cinema.plus.presentation.dto.ErrorDTO;
import com.cinema.plus.presentation.dto.FieldDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());

        List<FieldDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::createFieldDTO)
                .toList();

        ErrorDTO error = new ErrorDTO(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors,
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private FieldDTO createFieldDTO(FieldError fieldError) {
        String message = fieldError.getDefaultMessage();
        if (messageSource != null) {
            message = messageSource.getMessage(fieldError, Locale.ENGLISH);
        }
        return new FieldDTO(fieldError.getField(), message);
    }
}