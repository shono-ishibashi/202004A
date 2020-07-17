package com.controller;

import com.form.OrderConfirmForm;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/confirm")
public class OrderComfirmController {


    @ModelAttribute
    public OrderConfirmForm setUpform(){
        return new OrderConfirmForm();
    }

    @RequestMapping("")
    public String showConfirm(Model model){



        List<String> deliveryTimeList = new ArrayList<>();
        deliveryTimeList.add("10時");
        deliveryTimeList.add("11時");
        deliveryTimeList.add("12時");
        deliveryTimeList.add("13時");
        deliveryTimeList.add("14時");
        deliveryTimeList.add("15時");
        deliveryTimeList.add("16時");
        deliveryTimeList.add("17時");
        deliveryTimeList.add("18時");

        model.addAttribute("deliveryTimeList", deliveryTimeList);

        Map<Integer, String> paymentMap = new HashMap<>();
        paymentMap.put(1,"代金引換");
        paymentMap.put(2,"クレジットカード");

        model.addAttribute("paymentMap", paymentMap);
        return "order_confirm";
    }

    @RequestMapping("/order-finished")
    public String orderFinished(@Validated OrderConfirmForm orderConfirmForm, BindingResult result, Model model){

        if(result.hasErrors()){
            return showConfirm(model);
        }

        return "order_finished";
    }
}
