package com.controller;

import com.domain.Order;
import com.domain.User;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class ShowHistoryController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/show_history")
    public String showHistory(Model model){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> orderList = orderService.getOrderHistoryList(user.getId());

        model.addAttribute("orderList",orderList);


        return "order_history";
    }
}
