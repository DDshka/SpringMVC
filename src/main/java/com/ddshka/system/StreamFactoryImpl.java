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
        stream = new StreamInstance(user);
        return stream;
    }

    @Override
    public Stream setBroadcastStopEvent(BroadcastEvent event) {
        stream.setBroadcastStopEvent(event);
        return stream;
    }

    private class StreamInstance extends Stream {

        private String soundDestination;
        private String statusDestination;

        public StreamInstance(User user) {
            super(user);

            soundDestination = "/topic/" + user.getName();
            statusDestination = "/topic/dashboard/" + user.getName();
        }

        @Override
        public String sendTrackChange(String destination, Integer position) {
            String json = new JSONObject()
                    .put("position", position)
                    .toString();

            smt.convertAndSend(destination, json);
            return json;
        }

        public void sleep(int millis) {
            try {
                Thread.sleep(millis);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            broadcast = new Broadcast();
            broadcast.setUser(user);
            broadcastService.add(broadcast);

            Track track = trackService.getByPosition(user, position);
            if (track == null) {
                throw new NullPointerException("User " + user.getName() + " has no tracks to broadcast");
            }

            logger.log(Level.INFO, "[BROADCAST] " + user.getName() + " has started broadcast");
            while (track != null) {
                broadcast.setSongName(track.getArtist() + " - " + track.getTitle());
                broadcastService.update(broadcast);
                sendTrackChange(statusDestination, track.getPosition());

                sendPackages(track);

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
                next = previous = false;
                sleep(1000);
            }
            sendTrackChange(statusDestination, -1);
            broadcastService.delete(broadcast);
            onBroadcastStop();
            logger.log(Level.INFO, "[BROADCAST] " + user.getName() + "`s broadcast has ended successfully");
        }

        private void sendPackages(Track track) {
            byte[] file = track.getFile();
            int seconds = (int) trackService.getLength(file);
            int oneSecondPackage = file.length/seconds;
            byte[] buffer = new byte[oneSecondPackage];

            ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
            try {
                while (inputStream.read(buffer) != -1) {
                    while (broadcastInterrupted) {
                        Thread.sleep(1);
                    }

                    if (next || previous) {
                        break;
                    }

                    smt.convertAndSend(soundDestination, Arrays.toString(buffer));
                    sleep(1000);
                }
            }
            catch (Exception e) {
                error(e);
            }
        }

        private void error(Exception e) {
            broadcastService.delete(broadcast);
            onBroadcastStop();
            logger.log(Level.WARNING, "[BROADCAST] " + user.getName() + "`s broadcast has ended with exception. Stacktrace: ");
            e.printStackTrace();
        }
    }
}
