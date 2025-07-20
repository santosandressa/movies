package com.cinema.plus.presentation.dto;

import java.util.List;

public record ErrorDTO(
        String message,
        int status,
        List<FieldDTO> field,
        String timestamp

) { }
