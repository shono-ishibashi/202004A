package com.service;

import com.domain.Cart;
import com.domain.Item;
import com.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private HttpSession session;

    public Cart addCart(Item item) {
        List<OrderItem> cartInScope = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) {
            Cart cart = new Cart();

        }
        for (OrderItem orderItem : cart) {


        }return cart;
    }

