package com.ritik.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String homeControllerHandler() {
    return "Payment Microservice for Salon Booking System";
    }
}
