package com.service;

import com.domain.Cart;
import com.domain.Item;
import com.domain.Order;
import com.domain.OrderItem;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderRepository orderRepository;

    public Cart addCart() throws Exception {

        Integer testId = 1;
        List<Order> orderListStatus0 = orderRepository.findByUserIdAndStatus0(1);

        Order order;

        // status 0 の orderがあればそれを利用しなければ、インスタンス化する
        // それ以外は Exeptionを投げる
        if(orderListStatus0.isEmpty()){
            order = new Order();
            order.setStatus(0);
        }else if (orderListStatus0.size() == 1) {
            order = orderListStatus0.get(0);
        }else {
            throw new Exception();
        }





        order.getOrderItemList().add();
    }

    }

