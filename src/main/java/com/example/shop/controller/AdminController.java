package com.example.shop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    // Hardcoded credentials
    private static final String ADMIN_USERNAME1 = "Tarun";
    private static final String ADMIN_PASSWORD1 = "AlphaMarket";
    private static final String ADMIN_USERNAME2 = "Khushvant";
    private static final String ADMIN_PASSWORD2 = "Apple";
    private static final String ADMIN_USERNAME3 = "Himanshu";
    private static final String ADMIN_PASSWORD3 = "Priyanshi";
    private static final String ADMIN_USERNAME4 = "Mayank";
    private static final String ADMIN_PASSWORD4 = "0329";

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (ADMIN_USERNAME1.equals(username) && ADMIN_PASSWORD1.equals(password)) {
            // Set session attribute to indicate logged in
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            return "redirect:/cart/inventory";
        } else if (ADMIN_USERNAME2.equals(username) && ADMIN_PASSWORD2.equals(password)) {
            // Set session attribute to indicate logged in
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            return "redirect:/cart/inventory";
        } else if (ADMIN_USERNAME3.equals(username) && ADMIN_PASSWORD3.equals(password)) {
            // Set session attribute to indicate logged in
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            return "redirect:/cart/inventory";
        } else if (ADMIN_USERNAME4.equals(username) && ADMIN_PASSWORD4.equals(password)) {
            // Set session attribute to indicate logged in
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            return "redirect:/cart/inventory";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid User ID or Password");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/cart/inventory";
    }
}
