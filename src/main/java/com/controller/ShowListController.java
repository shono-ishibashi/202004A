package com.controller;

import com.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noodle")
public class ShowListController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/show-list")
    public String showList( Integer id, Model model) {

        return "item_list_noodle";
    }


}
