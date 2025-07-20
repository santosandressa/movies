package com.cinema.plus.presentation.controller;

import com.cinema.plus.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("authenticate")
    public String authenticate(Authentication authentication){
        return authService.authenticate(authentication);
    }

}
