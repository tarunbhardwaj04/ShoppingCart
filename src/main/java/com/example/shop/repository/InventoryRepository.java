package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shop.entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Integer> {
}
