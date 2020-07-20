package com.controller;

import com.domain.Order;
import com.domain.User;
import com.service.OrderHistoryService;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.List;

@Controller
@RequestMapping("/order")
public class ShowHistoryController {
@Autowired
private OrderHistoryService orderHistoryService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/show_history")
    public String showHistory(Model model){


        //Integer userId = (Integer)session.getAttribute("userId");
        //Integer userId = 4;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orderList = orderHistoryService.getOrderHistoryList(user.getId());
        if(!orderList.isEmpty()) {
            model.addAttribute("orderList", orderList);
        }else{
            model.addAttribute("emptyMessage","注文履歴がありません");
        }

        return "order_history";
    }

    @RequestMapping("/show_history/detail")
    public String showHistoryDetail(Model model, Integer orderId){
        Order orderHistory = orderHistoryService.getOrderHistoryDetail(orderId);
        model.addAttribute("orderHistory", orderHistory);
        return "order_history_detail";
    }
}
