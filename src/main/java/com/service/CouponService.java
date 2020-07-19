package com.service;

import com.domain.Coupon;
import com.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Coupon searchCoupon(String couponCode){
        return couponRepository.findByCouponCode(couponCode);
    }
}

