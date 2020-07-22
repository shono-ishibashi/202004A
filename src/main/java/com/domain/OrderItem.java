package com.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderItem {
    private Integer id;
    private Integer itemId;
    private Integer orderId;
    private Integer quantity;
    private Character size;
    private Item item;
    private Integer totalPrice;
    private List<OrderTopping> orderToppingList = new ArrayList<>();
    private Integer reviewPoint;

}
