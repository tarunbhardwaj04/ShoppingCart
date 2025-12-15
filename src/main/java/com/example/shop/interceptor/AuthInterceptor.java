package com.example.shop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Object isLoggedIn = session.getAttribute("isLoggedIn");

        if (isLoggedIn != null && (Boolean) isLoggedIn) {
            return true;
        }

        // If not logged in, redirect to login page
        response.sendRedirect("/login");
        return false;
    }
}
