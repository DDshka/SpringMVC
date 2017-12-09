package com.ddshka.dao;

import com.ddshka.model.Broadcast;
import com.ddshka.model.User;

import java.util.List;

public interface BroadcastDao {
    List<Broadcast> list();
    Broadcast get(User user);
    void add(Broadcast broadcast);
    void update(Broadcast broadcast);
    void delete(Broadcast broadcast);
}
