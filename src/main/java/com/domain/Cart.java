package com.domain;

import java.util.List;

public class Cart {
    private List<OrderItem> itemList;

    public void addItem(OrderItem orderItem){
        itemList.add(orderItem);
    }
    public void deleteItem(OrderItem orderItem){
        itemList.remove(orderItem);
    }
    public List getItemList(){return itemList;}
    public void setItemList(List<OrderItem> itemList){this.itemList = itemList;}

}
