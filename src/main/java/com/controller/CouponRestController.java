package com.controller;

import com.domain.Coupon;
import com.form.CouponForm;
import com.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponRestController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/search")
    public Coupon searchCoupon(String a){
            Coupon coupon = couponService.searchCoupon("test");
            return coupon;
    }
}
