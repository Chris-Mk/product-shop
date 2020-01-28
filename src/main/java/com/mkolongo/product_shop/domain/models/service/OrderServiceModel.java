package com.mkolongo.product_shop.domain.models.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderServiceModel {

    private String id;
    private String imageUrl;
    private String customerName;
    private BigDecimal totalPrice;
    private String orderDate;
    private String productName;
    private String productDescription;
    private int quantity;

}
