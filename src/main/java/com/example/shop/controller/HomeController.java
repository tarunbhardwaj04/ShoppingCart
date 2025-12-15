package com.example.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // This handles the root URL "localhost:9091/"
    @GetMapping("/")
    public String home() {
        // Automatically jumps to the inventory page
        return "redirect:/cart/addItem";
    }
}
