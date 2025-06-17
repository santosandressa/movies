package com.cinema.plus.presentation.dto;

public record ErrorDTO(
        String message,
        int status,
        String timestamp
) { }
