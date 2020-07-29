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
        //注文日を取得
        Date orderDate = order.getOrderDate();
        Date currentDate =new Date();
        //書式の作成
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年 MM月 dd日");
        String str = sdf.format(orderDate);
        String str1 = sdf.format(currentDate);
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
            paymentMsg = "代金引換";
        } else {
            paymentMsg = "クレジットカード決済";
        }

        //感謝のメッセージ
        StringBuilder builder1 = new StringBuilder();
        builder1.append(order.getDestinationName() + " 様 ");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));
        builder1.append("いつも「 ラクラクヌードル 」をご利用していただきまして、誠にありがとうございます。");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));
        builder1.append("ご注文が確定しました。");
        builder1.append(System.getProperty("line.separator"));
        builder1.append(System.getProperty("line.separator"));
        builder1.append("下記の注文内容をご確認ください。");
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
                //トッピングがなしの場合、Nullではなく"なし"を表示
                if(Objects.isNull( orderTopping.getTopping().getName())){
                    builder1.append("トッピング : なし");
                } else if(orderTopping == orderItem.getOrderToppingList().get(0) ){
                    builder1.append("トッピング : " + orderTopping.getTopping().getName());
                } else {
                    builder1.append("               : " + orderTopping.getTopping().getName());
                }
                builder1.append(System.getProperty("line.separator"));
            }
            builder1.append("数量 : " + orderItem.getQuantity() + " 杯") ;
            builder1.append(System.getProperty("line.separator"));
            double totalPriceD = orderItem.getTotalPrice() * 1.1;
            int totalPriceI = (int) totalPriceD;
            builder1.append("小計 : " + totalPriceI + " 円 (税込)");
            builder1.append(System.getProperty("line.separator"));
            builder1.append(System.getProperty("line.separator"));
        }


        //お客様情報をマップに詰める
        for( int i = 0 ; i < orderList.size() ; i++){
            orderMessageMap.put("注文日", str1 );
            orderMessageMap.put("配達時間", str  +  orderHour + "時");
            double totalPriceD = orderList.get(i).getTotalPrice() * 1.1;
            int totalPriceI = (int)totalPriceD;
            orderMessageMap.put("合計金額",+ totalPriceI +" 円 (税込)");
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
        msg.setSubject("ご注文が確定いたしました");
        msg.setText(builder2.toString());
        // メール送信
        mailSender.send(msg);
    }

}
