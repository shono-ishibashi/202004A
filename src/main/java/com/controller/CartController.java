package com.controller;

import com.domain.Cart;
import com.domain.Item;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private HttpSession session;

    @RequestMapping("/add")
    public String addItem(Item item){
    Cart cart = cartService.addCart(item);
    session.setAttribute("cart", cart);
    return "cart_list";
    }

}
