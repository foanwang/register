package com.at.register.service;

import com.at.register.entity.Coupon;
import com.at.register.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public void giveCoupon(String userId){
        Coupon coupon =  new Coupon();
        coupon.setUserId(userId);
        coupon.setUsed(false);
        Date date = new Date();
        coupon.setCreateTime(new Timestamp(date.getTime()));
        couponRepository.save(coupon);
    }
}
