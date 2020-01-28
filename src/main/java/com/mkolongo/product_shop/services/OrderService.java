package com.mkolongo.product_shop.services;

import com.mkolongo.product_shop.domain.models.service.OrderServiceModel;
import com.mkolongo.product_shop.domain.models.view.OrderDetailsModel;

import java.util.Set;

public interface OrderService {

    void addOrder(String productId, int quantity, String customerName);

    OrderDetailsModel getById(String id);

    Set<OrderServiceModel> getOrdersByCustomerName(String customerName);

    Set<OrderServiceModel> getAllOrders();

    void delete(String id);
}
