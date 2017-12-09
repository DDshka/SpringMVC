package com.ddshka.system;

import com.ddshka.model.Broadcast;
import com.ddshka.model.Track;
import com.ddshka.model.User;
import com.ddshka.service.BroadcastService;
import com.ddshka.service.TrackService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.logging.Level;

@Component
public class StreamFactoryImpl implements StreamFactory {

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private SimpMessagingTemplate smt;

    private Stream stream;

    @Override
    public Stream getStream(User user) {
        stream = buildStream(user);
        return stream;
    }

    @Override
    public Stream setBroadcastStopEvent(BroadcastEvent event) {
        stream.setBroadcastStopEvent(event);
        return stream;
    }

    private Stream buildStream(User user) {
        return new Stream(user) {
            @Override
            public void run() {
                try {
                    broadcast = new Broadcast();
                    broadcast.setUser(user);
                    broadcastService.add(broadcast);

                    String soundDestination = "/topic/" + user.getName();
                    String statusDestination = "/topic/dashboard/" + user.getName();

                    Track track = trackService.getByPosition(user, position);
                    sendTrackChange(statusDestination, track.getPosition());
                    logger.log(Level.INFO, "[BROADCAST] " + user.getName() + " has started broadcast");
                    while (track != null) {
                        byte[] file = track.getFile();
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);

                        broadcast.setSongName(
                                track.getArtist() + " - " + track.getTitle()
                        );
                        broadcastService.update(broadcast);

                        int seconds = (int) trackService.getLength(file);
                        int oneSecondPackage = file.length/seconds;

                        byte buffer[] = new byte[oneSecondPackage];
                        int count = inputStream.read(buffer);
                        while (count != -1) {
                            while (broadcastInterrupted) {
                                Thread.sleep(1);
                            }

                            if (next || previous) {
                                break;
                            }

                            count = inputStream.read(buffer);

                            smt.convertAndSend(soundDestination, Arrays.toString(buffer));

                            Thread.sleep(1000);
                        }

                        track = trackService.getById(user, track.getId());

                        if (previous) {
                            if (position.equals(0)) {
                                position = trackService.getLastPosition(user);
                            }
                            else {
                                position = track.getPosition() - 1;
                            }
                        }
                        else {
                            if (position.equals(trackService.getLastPosition(user))) {
                                break;
                            }
                            else {
                                position = track.getPosition() + 1;
                            }
                        }

                        track = trackService.getByPosition(user, position);
                        sendTrackChange(statusDestination, track.getPosition());

                        next = previous = false;
                        Thread.sleep(1000);
                    }
                    sendTrackChange(statusDestination, -1);
                    broadcastService.delete(broadcast);
                    onBroadcastStop();
                    logger.log(Level.INFO, "[BROADCAST] " + user.getName() + "`s broadcast has ended successfully");
                }
                catch (Exception e) {
                    broadcastService.delete(broadcast);
                    onBroadcastStop();
                    logger.log(Level.WARNING, "[BROADCAST] " + user.getName() + "`s broadcast has ended with exception. Stacktrace: ");
                    e.printStackTrace();
                }
            }

            @Override
            public String sendTrackChange(String destination, Integer position) {
                String json = new JSONObject()
                        .put("position", position)
                        .toString();

                smt.convertAndSend(destination, json);
                return json;
            }
        };
    }
}
