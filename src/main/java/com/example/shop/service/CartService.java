package com.example.shop.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.shop.entity.InventoryItem;
import com.example.shop.entity.CartItem;
import com.example.shop.repository.InventoryRepository;
import com.example.shop.repository.CartItemRepository;

@Service
public class CartService {

    private final InventoryRepository inventoryRepo;
    private final CartItemRepository cartRepo;

    public CartService(InventoryRepository inventoryRepo, CartItemRepository cartRepo) {
        this.inventoryRepo = inventoryRepo;
        this.cartRepo = cartRepo;
    }

    // --- INVENTORY MANAGEMENT ---

    public @NonNull InventoryItem addToInventory(@NonNull InventoryItem item) {
        return Objects.requireNonNull(inventoryRepo.save(item));
    }

    public List<InventoryItem> getInventory() {
        return inventoryRepo.findAll();
    }

    public Page<InventoryItem> getInventoryPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return inventoryRepo.findAll(pageable);
    }

    public InventoryItem getInventoryItem(int id) {
        return inventoryRepo.findById(id).orElse(null);
    }

    public void updateInventoryItem(@NonNull InventoryItem item) {
        inventoryRepo.save(item);
    }

    public void deleteInventoryItem(int id) {
        inventoryRepo.deleteById(id);
    }

    // --- CART MANAGEMENT ---

    public void addItemToCart(int inventoryId, int quantity) {
        InventoryItem inventoryItem = inventoryRepo.findById(inventoryId).orElse(null);
        if (inventoryItem == null)
            return;

        // Check if item already exists in cart
        List<CartItem> cartItems = cartRepo.findAll();
        CartItem existingItem = null;
        for (CartItem item : cartItems) {
            if (item.getInventoryItemId() != null && item.getInventoryItemId().equals(inventoryId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            int totalQuantity = existingItem.getQuantity() + quantity;
            if (totalQuantity > inventoryItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock! Available: " + inventoryItem.getQuantity());
            }
            existingItem.setQuantity(totalQuantity);
            cartRepo.save(existingItem);
        } else {
            if (quantity > inventoryItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock! Available: " + inventoryItem.getQuantity());
            }
            CartItem newItem = new CartItem(
                    inventoryItem.getName(),
                    inventoryItem.getPrice(),
                    quantity,
                    inventoryId);
            cartRepo.save(newItem);
        }
    }

    public List<CartItem> getCartItems() {
        return cartRepo.findAll();
    }

    public double calculateCartTotal() {
        double total = 0;
        for (CartItem item : getCartItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        clearCart(null);
    }

    // --- CHECKOUT ---

    public List<CartItem> getCartItems(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return cartRepo.findAll();
        }
        return cartRepo.findAllById(ids);
    }

    public double calculateCartTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            cartRepo.deleteAll();
        } else {
            cartRepo.deleteAllById(ids);
        }
        System.out.println("LOG: Shopping cart selection has been cleared.");
    }

    @Transactional
    public void finalizeTransaction(List<Integer> ids) {
        List<CartItem> cartItems = getCartItems(ids);
        for (CartItem cartItem : cartItems) {
            Integer inventoryId = cartItem.getInventoryItemId();
            if (inventoryId != null) {
                InventoryItem inventoryItem = inventoryRepo.findById(inventoryId).orElse(null);
                if (inventoryItem != null) {
                    int newQuantity = inventoryItem.getQuantity() - cartItem.getQuantity();
                    if (newQuantity < 0)
                        newQuantity = 0; // Prevent negative stock
                    inventoryItem.setQuantity(newQuantity);
                    inventoryRepo.save(inventoryItem);
                }
            }
        }
        System.out.println("LOG: Transaction finalized for selected items.");
    }

    @Transactional
    public void finalizeTransaction() {
        finalizeTransaction(null);
    }
}
