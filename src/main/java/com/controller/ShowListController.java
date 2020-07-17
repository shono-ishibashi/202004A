package com.controller;

import com.domain.Item;

import com.form.ItemForm;
import com.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/noodle")
public class ShowListController {

    @Autowired
    private ItemService itemService;

    @ModelAttribute
    private ItemForm setUpForm(){
        return new ItemForm();
    }

    /**
     *
     *商品一覧を表示する。
     * （商品の検索、オートコンプリート機能）
     * @param model
     * @return ページ表示のhtml
     */
    @RequestMapping("/show-list")
    public String showList(Model model) {
        Map<Integer, String> orderOfItemMap = new HashMap<>();
        orderOfItemMap.put(1, "値段が安い順");
        orderOfItemMap.put(2, "値段が高い順");
        orderOfItemMap.put(3,"人気順");
        model.addAttribute("orderOfItemMap", orderOfItemMap) ;
        List<Item> itemList = itemService.findAll();
        model.addAttribute("itemList", itemList);
        StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(itemList);
        model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
        return "item_list_noodle";
    }


    /**
     * 検索結果を表示する。
     *
     * @param itemForm
     * @param result
     * @param model
     * @return
     */
    @RequestMapping("/search_noodle")
    public String searchNoodle(@Validated ItemForm itemForm, String orderKey, BindingResult result, Model model){

        if(result.hasErrors()){
            return showList(model);
        }

        Map<Integer, String> orderOfItemMap = new HashMap<>();
        orderOfItemMap.put(1, "値段が安い順");
        orderOfItemMap.put(2, "値段が高い順");
        orderOfItemMap.put(3,"人気順");
        model.addAttribute("orderOfItemMap", orderOfItemMap) ;



        List<Item> allItems = itemService.findAll();
        List<Item> itemList = itemService.findByItem(itemForm.getNoodleName());
        if(itemList.size()==0){
            model.addAttribute("failure", "該当する商品がありません");
            model.addAttribute("itemList", allItems);
            StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(allItems);
            model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
            return "item_list_noodle";
        } else{
            model.addAttribute("itemList", itemList);
            StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(allItems);
            model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
            return "item_list_noodle";
        }


    }



}
