package com.ddshka.controller;

import com.ddshka.model.User;
import com.ddshka.model.UserDetails;
import com.ddshka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public abstract class AbstractController {

    protected static final String ANONYMOUS = "anonymousUser";

    @Autowired
    protected UserService userService;

    protected User getUser() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails user = null;
        if (auth instanceof UserDetails) {
            user = (UserDetails) auth;
        }
        else {
            return null;
        }

        return userService.get(user.getUsername());
    }

    protected User getUser(String username) {
        return userService.get(username);
    }
}
