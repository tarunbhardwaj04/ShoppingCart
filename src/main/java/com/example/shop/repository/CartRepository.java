package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
