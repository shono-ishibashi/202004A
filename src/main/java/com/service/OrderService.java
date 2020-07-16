package com.service;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.OrderTopping;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {


    @Autowired
    private OrderRepository orderRepository;

    public void UpDate(Order order){
        orderRepository.UpDate(order);
    }

}

