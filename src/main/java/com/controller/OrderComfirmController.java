package com.controller;

import com.domain.Order;
import com.form.OrderConfirmForm;
import com.service.CartService;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.sql.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;

@Controller
@RequestMapping("/confirm")
public class OrderComfirmController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderService orderService;

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
        deliveryTimeList.add("19時");
        deliveryTimeList.add("20時");

        model.addAttribute("deliveryTimeList", deliveryTimeList);

        Map<Integer, String> paymentMap = new HashMap<>();
        paymentMap.put(1,"代金引換");
        paymentMap.put(2,"クレジットカード");

        model.addAttribute("paymentMap", paymentMap);
        return "order_confirm";
    }

    /**
     *  注文ボタンを押下した時の処理
     */

    @RequestMapping("/order-finished")
    public String orderFinished(@Validated OrderConfirmForm orderConfirmForm, BindingResult result, Integer id, Integer status, Model model) throws Exception {

        if(result.hasErrors()){
            return showConfirm(id,status,model);
        }
        //Integer型としてuserIdを取得
//        Integer userId = (Integer)session.getAttribute("userId");
        Integer userId = 1;
        Order order = new Order();
        //String型の日付をDate型へ変換
        Date date = Date.valueOf(orderConfirmForm.getOrderDate());
        order.setOrderDate(date);
        order.setDestinationName(orderConfirmForm.getName());
        order.setDestinationEmail(orderConfirmForm.getMailAddress());
        //xxx-xxxxで受け取った郵便番号をxxxxxxxxの形に変更
        String zipCode = orderConfirmForm.getZipCode().replace("-", "");
        order.setDestinationZipcode(zipCode);
        //StringBuilderを使って住所を結合
        String address1 = orderConfirmForm.getAddress1();
        String address2 = orderConfirmForm.getAddress2();
        StringBuilder buf = new StringBuilder();
        buf.append(address1);
        buf.append(address2);
        order.setDestinationAddress(buf.toString());
        order.setDestinationTel(orderConfirmForm.getTelephone());
        //String型の日付をTimeStamp型へ変換
        //注文時刻（時間）を整数として取得
        String orderTimeStr = orderConfirmForm.getOrderTime();
        String replaceOrderTimeStr = orderTimeStr.replace("時", "");
        Integer replaceOrderTimeInt0 = Integer.parseInt(replaceOrderTimeStr);
        //LocalDateTime型に年月日、時間をいれる
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(orderConfirmForm.getOrderDate());
        LocalDate localDate = LocalDate.parse(orderConfirmForm.getOrderDate());
        LocalDateTime dd = localDate.atTime(replaceOrderTimeInt0,0,0);
        Timestamp ts = Timestamp.valueOf(dd);
        order.setDeliveryTime(ts);
        order.setPaymentMethod(orderConfirmForm.getPaymentMethod());

        //現在時刻（日）を取得
        LocalDate currentDate = LocalDate.now();
        //現在時刻（時間）を整数として取得
        LocalDateTime localDateTime = LocalDateTime.now();
        Integer currentDateTimeInt = localDateTime.getHour();
        //注文時刻（日）を取得
        String orderDateStr = orderConfirmForm.getOrderDate();
        LocalDate orderDate = LocalDate.parse(orderDateStr);
        //注文時刻（時間）を整数として取得
//        String orderTimeStr = orderConfirmForm.getOrderTime();
//        String replaceOrderTimeStr = orderTimeStr.replace("時", "");
        Integer replaceOrderTimeInt1 = Integer.parseInt(replaceOrderTimeStr);

        //現在時刻（日）と注文時刻（日）を比較
        if(orderDate.compareTo(currentDate) < 0 ){
            System.out.println("iiiii");
            model.addAttribute("errorMsg", "今から3時間後の日時をご入力ください");
            return showConfirm(id,status,model);
        } else if(orderDate.compareTo(currentDate) == 0 ){ //注文日が当日の場合
            //現在時刻を２つ生成、１つ現在時刻として、もう一つは現在時刻の時間の部分を注文時間を変更
            LocalDateTime currentDateTime1 = LocalDateTime.now();
            LocalDateTime currentDateTime2 = LocalDateTime.now();
            //現在時刻を注文時間に変更
            LocalDateTime orderDateTime = currentDateTime2.withHour(replaceOrderTimeInt1);
            //現在時刻に3時間加算
            LocalDateTime currentDateTimePlus3Hour = currentDateTime1.plusHours(3);
            //現在時刻と注文時刻が３時間離れているか比較
            if( orderDateTime.compareTo(currentDateTimePlus3Hour) < 0 ){
                System.out.println("aaaaaa");
                model.addAttribute("errorMsg", "今から3時間後の日時をご入力ください");
                return showConfirm(id,status,model);
            }
//            orderService.UpDate(order);
            System.out.println("あああああ");
            return "order_finished";
        }
//        orderService.UpDate(order);
        System.out.println("いいいいい");


        return "order_finished";
    }
    @RequestMapping("/view")
    public String past(Model model){
        //Integer userId = (Integer)session.getAttribute("userId");
        Integer userId = 1;
        List<Order> orderList = orderService.getOrderHistoryList(userId);
        model.addAttribute("orderList", orderList);
        return  "order_history";
    }
}
