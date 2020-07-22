package com.controller;

import com.common.NoodleGenre;
import com.domain.Item;

import com.domain.ItemPaging;
import com.form.ItemForm;
import com.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showList(@PageableDefault(page = 0, size = 9) Pageable pageable, Model model) {
        Page<ItemPaging> itemPage = itemService.findAllPage(pageable);
        model.addAttribute("page", itemPage);
        model.addAttribute("itemList", itemPage.getContent());

        Map<Integer, String> sortMap = new HashMap<>();
        sortMap.put(1, "値段が安い順");
        sortMap.put(2, "値段が高い順");
        sortMap.put(3, "人気順");
        model.addAttribute("sortMap", sortMap);

        List<Item> itemList = itemService.findAll();
//        StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(itemList);
        model.addAttribute("itemListForAutocomplete", itemList);


        model.addAttribute("genres", NoodleGenre.values());
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
    public String searchNoodle(
            Model model
            , ItemForm itemForm
            , @PageableDefault(page = 0, size = 9) Pageable pageable
    ) throws Exception {

        model.addAttribute("genres", NoodleGenre.values());

        Map<Integer, String> sortMap = new HashMap<>();
        sortMap.put(1, "値段が安い順");
        sortMap.put(2, "値段が高い順");
        sortMap.put(3, "人気順");
        model.addAttribute("sortMap", sortMap);

        Page<ItemPaging> page;
        Integer orderKey = itemForm.getSortNumber();

        if (itemForm.getNoodleName().isEmpty()) {
            System.out.println(orderKey);
            if (1 == orderKey) {
                page = itemService.findAllPage(pageable);
            } else if (2 == orderKey) {
                page = itemService.findAllByPriceDesc(pageable);
            } else if (3 == orderKey) {
                page = itemService.findAllPage(pageable);
            } else {
                page = itemService.findAllPage(pageable);
                model.addAttribute("sortError", true);
            }
        } else {
            page = itemService.findByName(itemForm.getNoodleName(), itemForm.getSortNumber(), pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("itemList", page.getContent());

        List<Item> allItems = itemService.findAll();
//        StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(allItems);
        model.addAttribute("itemListForAutocomplete", allItems);
        return "item_list_noodle";
    }

    /**
     *
     * ジャンルでラーメンを検索するメソッド
     *
     * @param genre
     * @param model
     * @return
     */
    @RequestMapping("/search_noodle/genre")
    public String searchByGenre(Integer genre, Model model, @PageableDefault(page = 0, size = 9) Pageable pageable) {

        //送られた数値が不正な値かどうかを確認する。
        boolean genreExists = false;
        for (NoodleGenre enumGenre : NoodleGenre.values()) {
            if (enumGenre.getLabelNum().equals(genre)) {
                genreExists = true;
            }
        }

        Page<ItemPaging> itemPage;

        //不正な値ならメッセージを投げて、全件表示させる
        if (genreExists) {
            itemPage = itemService.findByGenre(genre, pageable);
        } else {
            itemPage = itemService.findAllPage(pageable);
            model.addAttribute("genreError", true);
        }

        Map<Integer, String> sortMap = new HashMap<>();
        sortMap.put(1, "値段が安い順");
        sortMap.put(2, "値段が高い順");
        sortMap.put(3, "人気順");
        model.addAttribute("sortMap", sortMap);

        model.addAttribute("page", itemPage);
        model.addAttribute("itemList", itemPage.getContent());
        model.addAttribute("genres", NoodleGenre.values());

        List<Item> itemList = itemService.findAll();
//        StringBuilder itemListForAutocomplete = itemService.getNoodleAutoCompleteList(itemList);
        model.addAttribute("itemListForAutocomplete", itemList);

        return "item_list_noodle";
    }
}
