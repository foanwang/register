package com.at.register.service;

import com.at.register.constants.CreateType;
import com.at.register.constants.RegisterErrorCode;
import com.at.register.entity.User;
import com.at.register.exception.RegisterException;
import com.at.register.exception.RegisterExceptionFactory;
import com.at.register.payload.Response.SignUpResponse;
import com.at.register.repository.UserRepository;
import com.at.register.util.EmailAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User userRegister(String email, String username, String password, CreateType type){
        if(userRepository.existsByEmail(email)) {
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.EMAIL_ALREADY_REGISTERED.getMessage(), RegisterErrorCode.EMAIL_ALREADY_REGISTERED.getErrorCode());
        }
        if(password.length()<8) {
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.PASSWORD_IS_TOO_LESS.getMessage(), RegisterErrorCode.PASSWORD_IS_TOO_LESS.getErrorCode());
        }
        if(!EmailAddressUtils.isValidEmailAddress(email)) {
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.WRONG_EMAIL_FORMAT.getMessage(), RegisterErrorCode.WRONG_EMAIL_FORMAT.getErrorCode());
        }

        User user = new User();
        UUID uuid  =  UUID.randomUUID();
        user.setUserId(uuid.toString());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreateType(type);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userRepository.save(user);

        //Send success mail
        emailService.triggerEmail(email,"foanwang@gmail.com", "Thanks your register, Register success!");
        //given coupon
        couponService.giveCoupon(user.getUserId());

        return result;
    }

    public User findUserBy(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.NOT_FOUND_THE_USER.getMessage(), RegisterErrorCode.NOT_FOUND_THE_USER.getErrorCode()));

    }

    public User findUserByEmail(String Email){
        return userRepository.findByEmail(Email)
                .orElseThrow(() -> RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.NOT_FOUND_THE_USER.getMessage(), RegisterErrorCode.NOT_FOUND_THE_USER.getErrorCode()));

    }

}
