package com.domain;

import java.util.List;

public class OrderItem {
    private Integer id;
    private Integer itemId;
    private Integer orderId;
    private Integer quantity;
    private Character size;
    private Item item;
    private List<Topping> orderToppingList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Character getSize() {
        return size;
    }

    public void setSize(Character size) {
        this.size = size;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Topping> getOrderToppingList() {
        return orderToppingList;
    }

    public void setOrderToppingList(List<Topping> orderToppingList) {
        this.orderToppingList = orderToppingList;
    }
}
