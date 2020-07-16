package com.controller;

import com.domain.Order;
import com.form.OrderConfirmForm;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/confirm")
public class OrderComfirmController {

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpSession session;

    @ModelAttribute
    public OrderConfirmForm setUpform(){
        return new OrderConfirmForm();
    }

    @RequestMapping("")
    public String showConfirm(Integer id, Integer status, Model model) throws Exception{

        List<Order> orderConfirmList = cartService.showCart(id, status);
        session.setAttribute("orderConfirmList", orderConfirmList);

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
    public String orderFinished(@Validated OrderConfirmForm orderConfirmForm, BindingResult result, Integer id, Integer status, Model model) throws Exception {

        //現在時刻（日）を取得
        LocalDate currentDate = LocalDate.now();
        //現在時刻（時間）を整数として取得
        LocalDateTime localDateTime = LocalDateTime.now();
        Integer currentDateTimeInt = localDateTime.getHour();

        //注文時刻（日）を取得
        String orderDateStr = orderConfirmForm.getOrderDate();
        LocalDate orderDate = LocalDate.parse(orderDateStr);

        //注文時刻（時間）を整数として取得
        String orderTimeStr = orderConfirmForm.getOrderTime();
        String replaceOrderTimeStr = orderTimeStr.replace("時", "");
        Integer replaceOrderTimeInt = Integer.parseInt(replaceOrderTimeStr);

        //現在時刻（日）と注文時刻（日）を比較
        if(orderDate.compareTo(currentDate) < 0 ){
            return showConfirm(id,status,model);
        }

        //現在時刻（時間）と注文時刻（時間）を比較
        if(currentDateTimeInt + 3 > replaceOrderTimeInt){
            return showConfirm(id,status,model);
        }



        if(result.hasErrors()){
            return showConfirm(id,status,model);
        }

        return "order_finished";
    }
}
