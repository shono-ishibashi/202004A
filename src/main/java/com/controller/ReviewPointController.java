package com.controller;

import com.service.ReviewPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewPointController {
@Autowired
private ReviewPointService reviewPointService;

@Autowired
private  ShowHistoryController showHistoryController;
    @RequestMapping("")
    public String postReviewPoint(Integer orderItemId, Integer point, Integer orderItemItemId, Model model){
        reviewPointService.postReviewPointForOrderItem(orderItemId, point);
        reviewPointService.updateReviewPointForItem(orderItemId, orderItemItemId);
        return showHistoryController.showHistory(model);
    }

}
