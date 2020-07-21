package com.controller;

import com.domain.Cart;
import com.domain.Item;
import com.domain.Order;
import com.domain.User;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpSession session;


    @RequestMapping("/show-list")
    public String showCart(Model model) throws Exception {

        Integer userId = (Integer)session.getAttribute("userId");

        List<Order> cart = cartService.showCart(userId,0);

        model.addAttribute("cart", cart);

        return "cart_list";
    }
}
