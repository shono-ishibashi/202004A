package com.service;

import com.domain.Item;
import com.domain.OrderItem;
import com.repository.ReviewPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewPointService {
    @Autowired
    private ReviewPointRepository reviewPointRepository;

    public void postReviewPointForOrderItem(Integer orderItemId, Integer point){
        reviewPointRepository.postReviewPointForOrderItem(orderItemId,point);
    }

    public void updateReviewPointForItem(Integer orderItemId, Integer orderItemItemId){
        List<OrderItem> orderItemList = reviewPointRepository.getOrderItemByItemId(orderItemItemId);
        double orderItemCount = 0;
        double sumOfOrderItemPoints = 0;

        for(OrderItem orderItem: orderItemList){
            if(orderItem.getReviewPoint() != -1){
                orderItemCount ++;
                sumOfOrderItemPoints += orderItem.getReviewPoint();
            }
        }

        Double totalReviewPoint = sumOfOrderItemPoints / orderItemCount;
        Item item = reviewPointRepository.getItem(orderItemId);
        item.setReviewPoint(totalReviewPoint);
        reviewPointRepository.postReviewPointForItem(item);

    }
}
