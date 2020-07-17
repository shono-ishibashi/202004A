package com.service;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.OrderTopping;
import com.domain.User;
import com.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrderHistoryList(Integer userId){
        List<Order> orderListWithDub = orderRepository.findByUserIdJoinOrderItemsForOrderHistory(userId);

        List<Integer> orderItemIdList = new ArrayList<>();
        List<OrderItem> orderItemListWithoutDub = new ArrayList<>();
        List<Integer> orderIdList = new ArrayList<>();
        List<Order> orderListWithoutDub = new ArrayList<>();

        orderListWithDub.stream()
                .forEach(order -> orderItemIdList.add(order.getOrderItemList().get(0).getId()));

        List<Integer> orderItemIdListWithoutDub = new ArrayList<Integer>(new LinkedHashSet<>(orderItemIdList));

        for(int orderItemId: orderItemIdListWithoutDub){

            OrderItem orderItem = new OrderItem();
            List<OrderTopping> orderToppingList = new ArrayList<>();

            for(Order orderInList: orderListWithDub){
                if(orderItemId == orderInList.getOrderItemList().get(0).getId()){

                    orderItem.setId(orderInList.getOrderItemList().get(0).getId());
                    orderItem.setQuantity(orderInList.getOrderItemList().get(0).getQuantity());
                    orderItem.setItem(orderInList.getOrderItemList().get(0).getItem());
                    orderItem.setOrderId(orderInList.getOrderItemList().get(0).getOrderId());
                    orderItem.setSize(orderInList.getOrderItemList().get(0).getSize());
                    orderItem.setItemId(orderInList.getOrderItemList().get(0).getItemId());

                    OrderTopping orderTopping = new OrderTopping();
                    orderTopping.setId(orderInList.getOrderItemList().get(0).getOrderToppingList().get(0).getId());
                    orderTopping.setOrderItemId(orderInList.getOrderItemList().get(0).getOrderToppingList().get(0).getOrderItemId());
                    orderTopping.setTopping(orderInList.getOrderItemList().get(0).getOrderToppingList().get(0).getTopping());
                    orderTopping.setToppingId(orderInList.getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId());

                    if(orderItem.getSize().equals('M')){
                        orderTopping.setPrice(200);
                    }else if(orderItem.getSize().equals('L')){
                        orderTopping.setPrice(300);
                    }

                    orderToppingList.add(orderTopping);
                }
            }
            orderItem.setOrderToppingList(orderToppingList);
            orderItemListWithoutDub.add(orderItem);
        }


        orderListWithDub.stream()
                .forEach(order -> orderIdList.add(order.getId()));

        List<Integer> orderIdListWithoutDub = new ArrayList<Integer>(new LinkedHashSet<>(orderIdList));

        for (int orderId: orderIdListWithoutDub){

            Order order = new Order();
            Map<Integer,OrderItem> orderItemMap = new LinkedHashMap<>();

            for(Order orderInList: orderListWithDub) {
                if (orderId == orderInList.getId()) {
                    order.setId(orderInList.getId());
                    order.setUserId(orderInList.getUserId());
                    order.setStatus(orderInList.getStatus());
                    order.setTotalPrice(orderInList.getTotalPrice());
                    order.setOrderDate(orderInList.getOrderDate());
                    order.setDestinationName(orderInList.getDestinationName());
                    order.setDestinationEmail(orderInList.getDestinationEmail());
                    order.setDestinationZipcode(orderInList.getDestinationZipcode());
                    order.setDestinationAddress(orderInList.getDestinationAddress());
                    order.setDestinationTel(orderInList.getDestinationTel());
                    order.setDeliveryTime(orderInList.getDeliveryTime());
                    order.setPaymentMethod(orderInList.getPaymentMethod());
                    order.setUser(orderInList.getUser());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(orderInList.getOrderItemList().get(0).getId());
                    orderItem.setItemId(orderInList.getOrderItemList().get(0).getItemId());
                    orderItem.setSize(orderInList.getOrderItemList().get(0).getSize());
                    orderItem.setQuantity(orderInList.getOrderItemList().get(0).getQuantity());
                    orderItem.setOrderId(orderInList.getOrderItemList().get(0).getOrderId());
                    orderItem.setItem(orderInList.getOrderItemList().get(0).getItem());


                    orderItemMap.put(orderItem.getId(),orderItem);
                }
            }

                    List<OrderItem> list = new ArrayList<>(orderItemMap.values());
                    order.setOrderItemList(list);
                    orderListWithoutDub.add(order);

        }

        for(Order order: orderListWithoutDub){
            for(OrderItem orderItem: order.getOrderItemList()){
                for(OrderItem orderItem1: orderItemListWithoutDub){
                    if(orderItem.getId() == orderItem1.getId()){

                        orderItem.setOrderToppingList(orderItem1.getOrderToppingList());
                    }
                }
            }
        }
        return orderListWithoutDub;
    }
}

