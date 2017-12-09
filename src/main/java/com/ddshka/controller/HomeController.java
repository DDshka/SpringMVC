package com.ddshka.controller;

import com.ddshka.model.Broadcast;
import com.ddshka.service.BroadcastService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController extends AbstractController {

    @Autowired
    BroadcastService broadcastService;

    @RequestMapping(value = { "/home", "/" })
    public ModelAndView home() {
        List<Broadcast> broadcasts = broadcastService.list();
            return new ModelAndView("home")
                .addObject("broadcasts", broadcasts)
                .addObject("user", getUser());
    }

    @ResponseBody
    @RequestMapping(value = "/getBroadcastsList")
    public String getBroadcastsList() {
        List<Broadcast> broadcasts = broadcastService.list();

        return new JSONArray(broadcasts.toArray()).toString();
    }
}
