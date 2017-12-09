package com.ddshka.system;

import com.ddshka.model.User;

public interface StreamFactory {
    Stream getStream(User user);
    Stream setBroadcastStopEvent(BroadcastEvent event);
}
