package com.example.shop.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.shop.entity.InventoryItem;
import com.example.shop.service.CartService;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CartService service;

    @Override
    public void run(String... args) throws Exception {
        // Check if inventory is empty (rough check)
        // Since service doesn't have "count" or "isEmpty" exposed maybe,
        // I'll just check if getInventoryPaginated returns 0 items on page 1.

        long count = service.getInventoryPaginated(1, 1).getTotalElements();

        if (count == 0) {
            System.out.println("Seeding database with electrical products...");

            // 1. iPhone 9
            service.addToInventory(new InventoryItem(
                    "Iphone 9",
                    549.00,
                    50,
                    "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"));

            // 2. iPhone X
            service.addToInventory(new InventoryItem(
                    "iPhone X",
                    899.00,
                    30,
                    "https://cdn.dummyjson.com/product-images/2/thumbnail.jpg"));

            // 3. Samsung Universe 9
            service.addToInventory(new InventoryItem(
                    "Samsung Universe 9",
                    1249.00,
                    30,
                    "https://cdn.dummyjson.com/product-images/3/thumbnail.jpg"));

            // 4. OPPOF19
            service.addToInventory(new InventoryItem(
                    "OPPOF19",
                    280.00,
                    100,
                    "https://cdn.dummyjson.com/product-images/4/thumbnail.jpg"));

            // 5. Huawei P30
            service.addToInventory(new InventoryItem(
                    "Huawei P30",
                    499.00,
                    20,
                    "https://cdn.dummyjson.com/product-images/5/thumbnail.jpg"));

            // 6. MacBook Pro
            service.addToInventory(new InventoryItem(
                    "MacBook Pro",
                    1749.00,
                    15,
                    "https://cdn.dummyjson.com/product-images/6/thumbnail.png"));

            // 7. Samsung Galaxy Book
            service.addToInventory(new InventoryItem(
                    "Samsung Galaxy Book",
                    1499.00,
                    20,
                    "https://cdn.dummyjson.com/product-images/7/thumbnail.jpg"));

            // 8. Microsoft Surface Laptop 4
            service.addToInventory(new InventoryItem(
                    "Microsoft Surface Laptop 4",
                    1499.00,
                    25,
                    "https://cdn.dummyjson.com/product-images/8/thumbnail.jpg"));

            // 9. Infix HP
            service.addToInventory(new InventoryItem(
                    "Infinix INBOOK",
                    1099.00,
                    40,
                    "https://cdn.dummyjson.com/product-images/9/thumbnail.jpg"));

            // 10. HP Pavilion 15
            service.addToInventory(new InventoryItem(
                    "HP Pavilion 15-DK1056WM",
                    1099.00,
                    30,
                    "https://cdn.dummyjson.com/product-images/10/thumbnail.jpeg"));

            System.out.println("Seeding completed.");
        }
    }
}
