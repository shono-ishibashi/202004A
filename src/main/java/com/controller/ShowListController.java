package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noodle")
public class ShowListController {

    @RequestMapping("/show-list")
    public String showList() {
        return "item_list_noodle";
    }

    @RequestMapping("/show-detail")
    public String showDetail() {
        return "item_detail";
    }
}
