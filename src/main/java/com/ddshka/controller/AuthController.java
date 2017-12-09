package com.ddshka.controller;

import com.ddshka.model.User;
import com.ddshka.service.RoleService;
import com.ddshka.service.SecurityService;
import com.ddshka.service.UserService;
import com.ddshka.service.validators.LoginValidator;
import com.ddshka.service.validators.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RegistrationValidator validator;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value="/login")
    public ModelAndView signIn() {
        return new ModelAndView("loginform")
                .addObject("user", new User());
    }

    @RequestMapping(value = "/signUpSubmit", method = RequestMethod.POST)
    public String loginSubmit(User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "signUp";
        }

        user.setRole(roleService.get("user"));
        userService.saveOrUpdate(user);

        securityService.autologin(user.getName(), user.getPassword());

        return "redirect:/home";
    }

    @RequestMapping(value = "/signUp")
    public ModelAndView loginSubmit() {
        return new ModelAndView("signUp")
                .addObject("user", new User());
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
}
