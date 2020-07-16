package com.service;

import com.domain.*;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderRepository orderRepository;

    public Order addCart() throws Exception {

//        Integer testId = 1;
//        List<Order> orderListStatus0 = orderRepository.findByUserIdAndStatus0();
//
//        Order order;
//
//        // status 0 の orderがあればそれを利用しなければ、インスタンス化する
//        // それ以外は Exeptionを投げる
//        if (orderListStatus0.isEmpty()) {
//            order = new Order();
//            order.setStatus(0);
//        } else if (orderListStatus0.size() == 1) {
//            order = orderListStatus0.get(0);
//        } else {
//            throw new Exception();
//        }
//
//
//        order.getOrderItemList();
//
        return null;
    }

    /**
     * @param userId ログイン中または、仮発行のUserId
     * @param status 注文状態を示す数値
     * @return 注文(Order)が入ったリスト
     * @throws Exception
     */
    public List<Order> showCart(Integer userId, Integer status) throws Exception {

        List<Order> orderList = orderRepository.findByUserIdJoinOrderItems(1, 0);


        //orderToppingだけのListを作成
        List<OrderTopping> orderToppingList = new ArrayList<>();
        OrderTopping orderTopping;

        for (Order order : orderList) {
            orderTopping = order.getOrderItemList().get(0).getOrderToppingList().get(0);
            orderToppingList.add(orderTopping);
        }

        //orderItemだけのリストを作成
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem;
        for (Order order : orderList) {
            orderItem = order.getOrderItemList().get(0);
            orderItemList.add(orderItem);
        }

        //Orderの重複なしのMapを作成
        //key = order.id value = order
        Map<Integer, Order> orderMap = new HashMap<Integer, Order>();

        for (Order order : orderList) {
            orderMap.put(order.getId(), order);
            orderMap.get(order.getId()).getOrderItemList().clear();
        }

        //OrderItemの重複なしのMapを作成
        //key = orderItem.id  value = orderItem
        Map<Integer, OrderItem> orderItemMap = new HashMap<Integer, OrderItem>();
        for (OrderItem uniqorderItem : orderItemList) {
            orderItemMap.put(uniqorderItem.getId(), uniqorderItem);
        }


        //OrderItem の の中にtoppingを 格納
        for (OrderItem uniqOrderItem : orderItemMap.values()) {
            uniqOrderItem.getOrderToppingList().clear();
            for (OrderTopping resultOrderTopping : orderToppingList) {
                if (uniqOrderItem.getId().equals(resultOrderTopping.getOrderItemId())) {
                    uniqOrderItem.getOrderToppingList().add(resultOrderTopping);
                }
            }
        }

        List<OrderItem> resultOrderItemList = new ArrayList<>(orderItemMap.values());

        //Order の 中に OrderItemを格納
        List<Order> result = new ArrayList<>(orderMap.values());

        for (Order order : result) {
            for (OrderItem resultOrderItem : resultOrderItemList) {
                if (order.getId().equals(resultOrderItem.getOrderId())) {
                    order.getOrderItemList().add(resultOrderItem);
                }
            }
        }

        return result;

    }

}

