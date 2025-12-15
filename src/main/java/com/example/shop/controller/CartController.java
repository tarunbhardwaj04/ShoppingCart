package com.example.shop.controller;

import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import com.example.shop.entity.InventoryItem;
import com.example.shop.entity.CartItem;
import com.example.shop.service.CartService;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService service;

    @GetMapping("/add")
    public String addItemPage(Model model) {
        model.addAttribute("item", new InventoryItem());
        return "add-item";
    }

    @PostMapping("/addItem")
    public String addItem(@ModelAttribute @NonNull InventoryItem item, RedirectAttributes redirectAttributes) {
        service.addToInventory(item);
        redirectAttributes.addFlashAttribute("success", "Item added to inventory successfully!");
        return "redirect:/cart/inventory";
    }

    @GetMapping("/inventory")
    public String viewInventory(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        int pageSize = 10;
        Page<InventoryItem> page = service.getInventoryPaginated(pageNo, pageSize);
        List<InventoryItem> listInventory = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("inventory", listInventory);

        return "inventory";
    }

    @GetMapping({ "/updatePriceForm/{id}", "/updateNameForm/{id}", "/updateQuantityForm/{id}", "/updateForm/{id}" })
    public String showUpdateForm(@PathVariable int id, Model model) {
        model.addAttribute("item", service.getInventoryItem(id));
        return "update-item-form";
    }

    @PostMapping("/updateItem")
    public String updateItem(@ModelAttribute @NonNull InventoryItem item, RedirectAttributes redirectAttributes) {
        service.updateInventoryItem(item);
        redirectAttributes.addFlashAttribute("success", "Item updated successfully!");
        return "redirect:/cart/inventory";
    }

    // --- 3. INVENTORY ACTIONS ---

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable int id) {
        service.deleteInventoryItem(id);
        return "redirect:/cart/inventory";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id, RedirectAttributes redirectAttributes) {
        service.addItemToCart(id, 1);
        redirectAttributes.addFlashAttribute("success", "Item added to cart!");
        return "redirect:/cart/inventory";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam int id, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        try {
            service.addItemToCart(id, quantity);
            redirectAttributes.addFlashAttribute("success", "Item (" + quantity + ") added to cart!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart/inventory";
    }

    // --- 4. CART & BILL VIEWS/ACTIONS ---

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable int id, RedirectAttributes redirectAttributes) {
        service.removeCartItem(id);
        redirectAttributes.addFlashAttribute("success", "Item removed from cart and stock restored.");
        return "redirect:/cart/viewCart";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", service.getCartItems());
        model.addAttribute("cartTotal", service.calculateCartTotal());
        return "viewCart";
    }

    @GetMapping("/bill")
    public String viewBill(@RequestParam(required = false) List<Integer> selectedItems, Model model) {
        List<CartItem> items = service.getCartItems(selectedItems);
        double subTotal = service.calculateCartTotal(items);

        final double taxRate = 0.07;
        double taxAmount = subTotal * taxRate;
        double grandTotal = subTotal + taxAmount;

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("cartItems", items);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("taxAmount", taxAmount);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("currentDate", new Date());

        return "bill";
    }

    @org.springframework.beans.factory.annotation.Value("${admin.payment.upi-id}")
    private String adminUpiId;

    @PostMapping("/payment")
    public String viewPaymentPage(@RequestParam(required = false) List<Integer> selectedItems, Model model) {
        if (selectedItems == null || selectedItems.isEmpty()) {
            return "redirect:/cart/bill";
        }

        List<CartItem> items = service.getCartItems(selectedItems);
        double subTotal = service.calculateCartTotal(items);

        final double taxRate = 0.07;
        double taxAmount = subTotal * taxRate;
        double grandTotal = subTotal + taxAmount;

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("adminUpiId", adminUpiId);
        // Generate a random transaction ID for display
        model.addAttribute("transactionId", "TXN" + System.currentTimeMillis());

        return "payment";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam(required = false) List<Integer> selectedItems,
            RedirectAttributes redirectAttributes) {
        service.finalizeTransaction(selectedItems);
        service.clearCart(selectedItems);

        return "redirect:/cart/inventory";
    }
}