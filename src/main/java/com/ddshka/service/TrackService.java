package com.ddshka.service;

import com.ddshka.dao.TrackDao;
import com.ddshka.model.TrackInfo;
import com.ddshka.model.User;
import com.mpatric.mp3agic.ID3v2;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface TrackService extends TrackDao {
    void updatePositions(User user, Integer prevPosition, Integer newPosition);

    ID3v2 getTrackInfo(byte[] bytes);

    long getLength(byte[] bytes);

    void delete(User user, List<Integer> positions);

    List<TrackInfo> add(User user, CommonsMultipartFile[] files);
}
