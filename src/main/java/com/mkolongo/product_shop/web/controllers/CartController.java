package com.mkolongo.product_shop.web.controllers;

import com.mkolongo.product_shop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public String details(Model model, Principal principal) {
        var orders = orderService.getOrdersByCustomerName(principal.getName());
        final BigDecimal totalPrice = orders.stream()
                .map(o -> o.getTotalPrice().multiply(BigDecimal.valueOf(o.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("model", orders);
        model.addAttribute("price", totalPrice);
        return "cart-details";
    }

    @PostMapping("/checkout")
    public String checkout() {
        return "redirect:/users/home";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable String id) {
        orderService.delete(id);
        return "redirect:/cart/details";
    }
}
