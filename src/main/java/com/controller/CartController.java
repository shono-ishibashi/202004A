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

    @RequestMapping("/add")
    public String addItem(Item item){


    return "cart_list";
    }

    @RequestMapping("/show-list")
    public String showCart(Model model){

        User user = (User)session.getAttribute("loginUser");
        if(isNull(user)){
            model.addAttribute("カートが空です");
            return "cart_list";
        }
        List<Order> cartList = cartService.showCart(user.getId());
        model.addAttribute("cartList", cartList);
        return "cart_list";
    }
}
