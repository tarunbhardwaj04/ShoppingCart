package com.example.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByInventoryItemId(Integer inventoryItemId);
}
