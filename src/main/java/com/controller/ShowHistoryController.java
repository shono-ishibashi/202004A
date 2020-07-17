package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class ShowHistoryController {

    @RequestMapping("/show_history")
    public String showHistory(){
        return "order_history";
    }
}
