package com.domain;

import lombok.Data;

@Data
public class OrderTopping {
    private Integer id;
    private Integer toppingId;
    private Integer orderItemId;
    private Topping topping;
    private Integer price;
}
