package com.at.register.service;

import com.at.register.constants.RegisterErrorCode;
import com.at.register.entity.User;
import com.at.register.exception.RegisterException;
import com.at.register.exception.RegisterExceptionFactory;
import com.at.register.payload.Response.LoginResponse;
import com.at.register.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public LoginResponse authlogin(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User loginuser =userRepository.findByEmail(email).get();
        if(loginuser == null){
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getMessage(), RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getErrorCode());
        }
        if(!passwordEncoder.matches(password, loginuser.getPassword())){
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.PASSWORD_IS_NOT_CORRECT.getMessage(), RegisterErrorCode.PASSWORD_IS_NOT_CORRECT.getErrorCode());
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(loginuser);
        loginResponse.setToken(loginuser.getUserId().toString());
        return loginResponse;
    }
}
