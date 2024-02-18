package com.web.finance.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestController {
    @GetMapping("/auth/signIn")
    public String getSignIn() {
        return "authorization";
    }

    @GetMapping("/auth/signUp")
    public String getSignUp() {
        return "registration";
    }

    @GetMapping("/romantic")
    public String getrRomantic() {
        return "romantic";
    }
}