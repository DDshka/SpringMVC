package com.ddshka.dao;

import com.ddshka.model.Track;
import com.ddshka.model.TrackInfo;
import com.ddshka.model.User;

import java.util.List;

public interface TrackDao {

    List<Track> list(User user);
    List<TrackInfo> listInfo(User user);
    Track getByPosition(User user, Integer position);
    Track getById(User user, Integer id);
    Integer getLastPosition(User user);
    void delete(Track track);
    void add(Track track);
    void update(Track track);
}
