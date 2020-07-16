package com.controller;

import com.service.CartService;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/delete")
public class OrderDeleteController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/item")
    public String delete(Integer orderId, Integer orderItemId){
        cartService.delete(orderId, orderItemId);
        return "forword:/cart/cart-list";
    }
}
