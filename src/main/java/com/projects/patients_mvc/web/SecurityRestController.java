package com.projects.patients_mvc.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityRestController {

    @GetMapping("/auth")
    public Authentication authentication (Authentication authentication) {
        return authentication;
    }
}
