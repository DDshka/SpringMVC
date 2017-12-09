package com.ddshka.controller;

import com.ddshka.model.*;
import com.ddshka.service.BroadcastService;
import com.ddshka.service.TrackService;
import com.ddshka.system.Stream;
import com.ddshka.system.StreamFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

@Controller
public class DashboardController extends AbstractController {

    @Autowired
    private SimpMessagingTemplate smt;

    @Autowired
    private TrackService trackService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private StreamFactory streamFactory;

    private HashMap<User, Stream> streams = new HashMap<>();

    private Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

    @RequestMapping("/dashboard")
    public ModelAndView dashboard() {
        User user = getUser();
        List<TrackInfo> tracks = trackService.listInfo(user);
        return new ModelAndView("dashboard")
                .addObject("user", user)
                .addObject("tracks", tracks);
    }

    @ResponseBody
    @RequestMapping(value="/upload")
    public String upload(@RequestParam(required = false) CommonsMultipartFile[] files) {
        User user = getUser();
        List<TrackInfo> tracks = trackService.add(user, files);

        JSONArray array = new JSONArray();
        for (TrackInfo track : tracks) {
            array.put(new JSONObject()
                    .put("artist", track.getArtist())
                    .put("title", track.getTitle()));
        }

        return array.toString();
    }

    @ResponseBody
    @RequestMapping(value="/play")
    public String play() {
        User user = getUser();
        if (streams.containsKey(user)) {
            Stream stream = streams.get(user);
            if (stream.isBroadcastInterrupted()) {
                stream.setBroadcastInterrupted(false);
            }
        }
        else {
            broadcast(user);
        }

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @ResponseBody
    @RequestMapping(value="/pause")
    public String pause() {
        User user = getUser();
        if (streams.containsKey(user)) {
            Stream stream = streams.get(user);
            if (!stream.isBroadcastInterrupted()) {
                stream.setBroadcastInterrupted(true);
            }
        }

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @ResponseBody
    @RequestMapping(value="/previous")
    public String previous() {
        User user = getUser();
        if (streams.containsKey(user)) {
            Stream stream = streams.get(user);
            stream.setPrevious(true);
        }

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @ResponseBody
    @RequestMapping(value="/next")
    public String next() {
        User user = getUser();
        if (streams.containsKey(user)) {
            Stream stream = streams.get(user);
            stream.setNext(true);
        }

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @ResponseBody
    @RequestMapping(value="/delete")
    public String delete(@RequestBody String positions) {
        User user = getUser();
        JSONArray array = new JSONArray(positions);

        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            pos.add(array.getInt(i));
        }

        trackService.delete(user, pos);

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @RequestMapping(value = "/changeTrackPosition")
    public @ResponseBody String changeTrackPosition(@RequestParam Integer previousPosition,
                                                    @RequestParam Integer newPosition) {
        User user = getUser();

        trackService.updatePositions(user, previousPosition, newPosition);

        return new JSONObject()
                .put("Success", true)
                .toString();
    }

    @MessageMapping("/topic/dashboard/status/{username}")
    @SendTo("/topic/dashboard/{username}")
    public String getStatus(@PathVariable String username) {

        User user = getUser(username);
        JSONObject json = new JSONObject();
        if (streams.containsKey(user)) {
            json.put(
                "position", streams.get(user).getPosition()
            );
        }
        return json.toString();
    }

    private void broadcast(User user) {
        Stream stream = streamFactory.getStream(user);
        stream.setBroadcastStopEvent(() -> streams.remove(user));
        streams.put(user, stream);
        stream.start();
    }
}
