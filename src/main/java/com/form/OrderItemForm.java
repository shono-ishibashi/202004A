package com.form;

import com.domain.Item;
import com.domain.OrderTopping;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemForm {
    private Integer id;
    private Integer itemId;
    private Integer orderId;
    private Character size;
    private Item item;
    private List<OrderTopping> orderToppingList;
}
