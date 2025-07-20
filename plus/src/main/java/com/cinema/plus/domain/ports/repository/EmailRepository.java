package com.cinema.plus.domain.ports.repository;

public interface EmailRepository {
    void sendVerificationEmail(String to, String username, String verificationCode);

    void sendPasswordResetEmail(String to, String username, String resetToken);
}