package com.at.register.controller;

import com.at.register.entity.User;
import com.at.register.security.model.UserPrincipal;
import com.at.register.security.model.CurrentUser;
import com.at.register.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-version}")
@Api(value = "${api-version}", tags = "UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findUserBy(userPrincipal.getId());
    }

}
