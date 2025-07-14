package com.cinema.plus.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiryDate;
}
