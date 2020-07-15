package com.controller;

import com.domain.Item;
import com.domain.Topping;
import com.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/noodle")
public class ShowDetailController {

    @Autowired
    private ItemService itemService;



    @RequestMapping("/show-detail")
    public String showDetail(String id, Model model ) {


        //商品選択時に受け取ったString型のidをLong型に変換


        Item item = itemService.load(Long.parseLong(id));

        model.addAttribute("item" , item );

        List<Topping> toppingList = itemService.toppingFindAll();

        model.addAttribute("toppingList", toppingList );

        return "item_detail";
    }

}
