package com.at.register.controller;

import com.at.register.constants.CreateType;
import com.at.register.entity.User;
import com.at.register.payload.Request.LoginRequest;
import com.at.register.payload.Request.SignUpRequest;
import com.at.register.payload.Response.LoginResponse;
import com.at.register.payload.Response.SignUpResponse;
import com.at.register.service.AuthService;
import com.at.register.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("${api-version}")
@Api(value = "${api-version}", tags = "AuthController")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @ApiOperation(value ="/login", notes = "User login API")
    @RequestMapping(value ="/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<LoginResponse> Login(HttpServletRequest request, LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authlogin(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }


    @ApiOperation(value ="/signup", notes = "create user")
    @RequestMapping(value ="/signup", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<SignUpResponse> Register(HttpServletRequest request, SignUpRequest signUpRequest) {
        SignUpResponse response = new SignUpResponse();
        User user = userService.userRegister(signUpRequest.getEmail(),signUpRequest.getUsername(), signUpRequest.getPassword(), CreateType.Local);
        response.setUser(user);
        response.setToken(user.getUserId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(user.getId()).toUri();
        return new ResponseEntity<>(response, HttpStatus.OK);

        //return new ResponseEntity<>(userService.Register(signUpRequest.getEmail(),signUpRequest.getUsername(), signUpRequest.getPassword()), HttpStatus.OK);
    }
}
