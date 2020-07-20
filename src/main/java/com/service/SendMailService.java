package com.service;


import com.domain.Order;
import com.domain.OrderItem;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class SendMailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private OrderRepository orderRepository;

    public void sendMail(Order order){
        //カートに入っている情報を取得
        List<Order> orderList = orderRepository.findByUserIdJoinOrderItems(order.getUserId(), order.getStatus());
        //送信する情報だけをつめるマップを生成
        Map<String, String> orderMessageMap = new HashMap<>();

        orderList.get(0).getOrderItemList();

        //カートの情報の文だけ回す
        for( int i = 0 ; i < orderList.size() ; i++){

            orderMessageMap.put("注文日", orderList.get(i).getOrderDate().toString());
            orderMessageMap.put("注文商品",orderList.get(i).getOrderItemList().toString());
            orderMessageMap.put("トッピング",orderList.get(i).getOrderItemList().get(i).getOrderToppingList().toString());
            orderMessageMap.put("合計金額",order.getTotalPrice().toString());
            orderMessageMap.put("お名前",orderList.get(i).getDestinationName());
            orderMessageMap.put("メールアドレス",orderList.get(i).getDestinationEmail());
            orderMessageMap.put("郵便番号",orderList.get(i).getDestinationZipcode());
            orderMessageMap.put("住所",orderList.get(i).getDestinationAddress());
            orderMessageMap.put("電話番号",orderList.get(i).getDestinationTel());
            orderMessageMap.put("配達時間",orderList.get(i).getDeliveryTime().toString());
            orderMessageMap.put("支払い方法",orderList.get(i).getPaymentMethod().toString());

        }

        StringBuilder builder = new StringBuilder();
        builder.append("ご注文ありがとうございます");
        builder.append(System.getProperty("line.separator"));
        builder.append(System.getProperty("line.separator"));
        builder.append("ご注文内容は以下の通りです");
        builder.append(System.getProperty("line.separator"));
        builder.append(System.getProperty("line.separator"));
        builder.append(" ＜ 注文内容 ＞ ");
        builder.append(System.getProperty("line.separator"));
        builder.append(System.getProperty("line.separator"));

        for (Map.Entry<String,String> orderMessageList : orderMessageMap.entrySet() ){
            builder.append(orderMessageList.getKey() + " : " + orderMessageList.getValue());
            builder.append(System.getProperty("line.separator"));
        }

        System.out.println(builder.toString());

        //        //partnersではできない
        SimpleMailMessage msg = new SimpleMailMessage();
        //フォームに入力されたメールアドレスをセット
        msg.setTo(order.getDestinationEmail());
//        msg.setCc("送信先メールアドレス2", "送信先メールアドレス3");
//        msg.setBcc("送信先メールアドレス4");
        msg.setSubject("注文完了のお知らせ");
        msg.setText(builder.toString());
        // メール送信
        mailSender.send(msg);

    }


}
