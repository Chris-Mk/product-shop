package com.mkolongo.product_shop.web.controllers;

import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;
import com.mkolongo.product_shop.domain.models.view.OrderViewModel;
import com.mkolongo.product_shop.services.OrderService;
import com.mkolongo.product_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ModelMapper mapper;

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public String my(Model model, Principal principal) {
        model.addAttribute("model", orderService.getOrdersByCustomerName(principal.getName()));
        return "my-orders";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public String all(Model model) {
        model.addAttribute("model", orderService.getAllOrders());
        return "all-orders";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String makeOrder(@PathVariable("id") String productId, Model model) {
        model.addAttribute("model", productService.getById(productId));
        return "order-product";
    }

    @PostMapping
    public String makeOrder(@RequestParam("id") String productId,
                            @RequestParam("quantity") byte quantity,
                            Principal principal) {
        orderService.addOrder(productId, quantity, principal.getName());
        return "redirect:/orders/my";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") String orderId, Model model) {
        final OrderDetailsModel byId = orderService.getById(orderId);
        model.addAttribute("model", byId);
        return "order-details";
    }

    @ResponseBody
    @GetMapping("/fetch")
    @PreAuthorize("isAuthenticated()")
    public List<OrderViewModel> fetch(Principal principal) {
        final Set<OrderServiceModel> allOrders = orderService.getOrdersByCustomerName(principal.getName());
        return mapper.map(allOrders, new TypeToken<List<OrderViewModel>>() {}.getType());
    }
}
