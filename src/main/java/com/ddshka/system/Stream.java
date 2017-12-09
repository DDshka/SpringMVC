package com.ddshka.system;

import com.ddshka.model.Broadcast;
import com.ddshka.model.User;
import org.json.JSONObject;

import java.util.logging.Logger;

public abstract class Stream extends Thread{
    protected Integer position;
    protected boolean next;
    protected boolean previous;
    protected boolean broadcastInterrupted;
    protected User user;
    protected Broadcast broadcast;
    protected Logger logger = Logger.getLogger(this.getName());
    protected BroadcastEvent event;

    public Stream(User user) {
        this.position = 0;
        this.user = user;
    }

    public final Integer getPosition() {
        return position;
    }

    public final void setNext(boolean next) {
        this.next = next;
    }

    public final void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public final boolean isBroadcastInterrupted() {
        return broadcastInterrupted;
    }

    public final void setBroadcastInterrupted(boolean interrupted) {
        this.broadcastInterrupted = interrupted;
    }

    public void setBroadcastStopEvent(BroadcastEvent event) {
        this.event = event;
    }

    public abstract String sendTrackChange(String destination, Integer position);

    public abstract void run();

    protected void onBroadcastStop() {
        if (event != null) {
            event.onBroadcastEnd();
        }
    }
}
