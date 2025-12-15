package com.example.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.shop.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@org.springframework.lang.NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/cart/add", "/cart/addItem", "/cart/delete/**", "/cart/update**")
                .excludePathPatterns("/cart/inventory", "/cart/viewCart", "/cart/bill", "/login", "/css/**", "/js/**",
                        "/images/**");
    }
}
