package com.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="coupon_code")
    private String couponCode;

    @Column(name="discount_rate")
    private Integer discountRate;
}
