package com.service;


import com.domain.Order;
import com.domain.OrderItem;
import com.domain.OrderTopping;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        Date o = order.getOrderDate();
        //書式の作成
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年 MM月 dd日");
        String str = sdf.format(o);
        Timestamp ts = order.getDeliveryTime();
        // Date型へ変換
        Date date = new Date(ts.getTime());
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Integer orderHour  = localDateTime.getHour();

        String orderZipcode = order.getDestinationZipcode();
        StringBuilder buf = new StringBuilder();
        buf.append(orderZipcode);
        buf.insert(3,"-");
        //郵便番号に"-"をいれる
        String newOrderZipcode = buf.toString();

        String paymentMsg;
        if (order.getPaymentMethod() == 1){
            paymentMsg = "未入金";
        } else {
            paymentMsg = "入金済";
        }

        //感謝のメッセージ
        StringBuilder builder1 = new StringBuilder();
        builder1.append("ご注文ありがとうございます");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));
        builder1.append("ご注文内容は以下の通りです");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));
        builder1.append(" ＜ ご注文内容 ＞ ");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));

        //商品情報をリストに詰める
        for(OrderItem orderItem :order.getOrderItemList()){
            builder1.append("商品名 : " + orderItem.getItem().getName());
            builder1.append(System.getProperty("line.separator"));
            builder1.append("商品サイズ : " + orderItem.getSize() + " サイズ");
            builder1.append(System.getProperty("line.separator"));
            for(OrderTopping orderTopping: orderItem.getOrderToppingList()){
                builder1.append("トッピング : " + orderTopping.getTopping().getName());
                builder1.append(System.getProperty("line.separator"));
            }
            builder1.append("数量 : " + orderItem.getQuantity() + " 杯") ;
            builder1.append(System.getProperty("line.separator"));
            builder1.append("小計 : " + orderItem.getTotalPrice() + " 円");
            builder1.append(System.getProperty("line.separator"));
        }
        System.out.println(builder1.toString());

        //お客様情報をマップに詰める
        for( int i = 0 ; i < orderList.size() ; i++){
            orderMessageMap.put("注文日 / 配達時間", str + " / " + orderHour.toString() + "時");
            orderMessageMap.put("合計金額",orderList.get(i).getTotalPrice().toString() + " 円");
            orderMessageMap.put("お名前",order.getDestinationName() + " 様");
            orderMessageMap.put("メールアドレス",order.getDestinationEmail());
            orderMessageMap.put("郵便番号","〒" + newOrderZipcode);
            orderMessageMap.put("住所",order.getDestinationAddress());
            orderMessageMap.put("電話番号",order.getDestinationTel());
            orderMessageMap.put("支払い方法",paymentMsg);
        }

        //注文内容
        StringBuilder builder = new StringBuilder();

        builder.append(" ＜ お届け先情報 ＞ ");
        builder.append(System.getProperty("line.separator"));

        for (Map.Entry<String,String> orderMessageList : orderMessageMap.entrySet() ){
            builder.append(orderMessageList.getKey() + " : " + orderMessageList.getValue());
            builder.append(System.getProperty("line.separator"));
        }
        System.out.println(builder.toString());



        StringBuilder builder2 = new StringBuilder();

        builder2.append(builder1.toString());
        builder2.append(System.getProperty("line.separator"));
        builder2.append(System.getProperty("line.separator"));
        builder2.append(builder.toString());


        //partnersではできない
        SimpleMailMessage msg = new SimpleMailMessage();
        //フォームに入力されたメールアドレスをセット
        msg.setTo(order.getDestinationEmail());
//        msg.setCc("送信先メールアドレス2", "送信先メールアドレス3");
//        msg.setBcc("送信先メールアドレス4");
        msg.setSubject("注文完了のお知らせ");
        msg.setText(builder2.toString());
        // メール送信
        mailSender.send(msg);
    }




}
