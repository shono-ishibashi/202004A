package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noodle")
public class ShowDetailController {


    @RequestMapping("/show-detail")
    public String showDetail() {
        return "item_detail";
    }

}
