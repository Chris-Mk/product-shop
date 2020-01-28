package com.mkolongo.product_shop.domain.models.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailsModel {

    private String id;
    private String imageUrl;
    private String productName;
    private String productDescription;
    private String customerName;
    private int quantity;
    private BigDecimal totalPrice;
    private String orderDate;

}
