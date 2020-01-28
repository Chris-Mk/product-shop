package com.mkolongo.product_shop.domain.models.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderViewModel {

    private String productName;
    private int quantity;
}
