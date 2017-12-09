package com.ddshka.service.impl;

import com.ddshka.dao.TrackDao;
import com.ddshka.model.Track;
import com.ddshka.model.TrackInfo;
import com.ddshka.model.User;
import com.ddshka.service.TrackService;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    @Qualifier("TrackDAO")
    private TrackDao trackDao;

    @Override
    @Transactional
    public List<Track> list(User user) {
        return trackDao.list(user);
    }

    @Override
    @Transactional
    public List<TrackInfo> listInfo(User user) {
        return trackDao.listInfo(user);
    }

    @Override
    @Transactional
    public void add(Track track) {
        trackDao.add(track);
    }

    @Override
    @Transactional
    public void update(Track track) {
        trackDao.update(track);
    }

    @Override
    @Transactional
    public void delete(Track track) {
        trackDao.delete(track);
    }

    @Override
    @Transactional
    public Integer getLastPosition(User user) {
        return trackDao.getLastPosition(user);
    }

    @Override
    @Transactional
    public Track getByPosition(User user, Integer position) {
        return trackDao.getByPosition(user, position);
    }

    @Override
    @Transactional
    public Track getById(User user, Integer id) {
        return  trackDao.getById(user, id);
    }

    @Override
    @Transactional
    public void delete(User user, List<Integer> positions) {
        List<Track> list = list(user);

        boolean ordered = true;
        for (int i = 0; i < positions.size() - 1; i++) {
            if (positions.get(i + 1) - positions.get(i) > 1) {
                ordered = false;
                break;
            }
        }

        if (ordered) {
            int minPos = positions.get(0);
            int maxPos = positions.get(positions.size() - 1);

            for (int i = minPos; i <= maxPos; i++) {
                delete(list.get(i));
            }

            for (int i = maxPos + 1; i < list.size(); i++) {
                Track track = list.get(i);
                track.setPosition(track.getPosition() - positions.size());
                update(track);
            }
        }
        else {
            for (int i = 0; i < positions.size(); i++) {
                int pos = positions.get(i);
                delete(list.get(pos));
            }

            int reduction = 1;
            for (int i = 0; i < positions.size(); i++) {
                if (i == positions.size() - 1) {
                    for (int j = positions.get(positions.size() - 1) + 1; j < list.size(); j++) {
                        Track track = list.get(j);
                        track.setPosition(track.getPosition() - reduction);
                        update(track);
                    }
                }
                else {
                    for (int j = positions.get(i) + 1; j < positions.get(i + 1); j++) {
                        Track track = list.get(j);
                        track.setPosition(track.getPosition() - reduction);
                        update(track);
                    }
                    reduction += 1;
                }
            }
        }
    }

    @Override
    @Transactional
    public List<TrackInfo> add(User user, CommonsMultipartFile[] files) {
        List<TrackInfo> tracks = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Integer order = getLastPosition(user);
            order = (order != null) ? order + 1 : 0;

            ID3v2 tags = getTrackInfo(files[i].getBytes());

            Track track = new Track();
                track.setUser(user);
                track.setFile(files[i].getBytes());
                track.setTimestamp(new Timestamp(System.currentTimeMillis()));
                track.setPosition(order);
                track.setArtist(tags.getArtist());
                track.setTitle(tags.getTitle());
                track.setComposer(tags.getComposer());
                track.setAlbum(tags.getAlbum());
                track.setAlbumImg(tags.getAlbumImage());
            add(track);

            tracks.add(track.toTrackInfo());
        }
        return tracks;
    }

    @Override
    @Transactional
    public void updatePositions(User user, Integer prevPosition, Integer newPosition) {
        List<Track> tracks  = trackDao.list(user);

        if (newPosition > prevPosition) {
            for (int i = prevPosition + 1; i <= newPosition; i++) {
                Track track = tracks.get(i);
                track.setPosition(i - 1);
                trackDao.update(track);
            }
        }
        else {
            for (int i = newPosition; i < prevPosition; i++) {
                Track track = tracks.get(i);
                track.setPosition(i + 1);
                trackDao.update(track);
            }
        }

        Track targetTrack = tracks.get(prevPosition);
        targetTrack.setPosition(newPosition);
        trackDao.update(targetTrack);
    }

    // TODO: this stuff is shit! RECODE
    public synchronized ID3v2 getTrackInfo(byte[] bytes) {
        File file = null;
        Mp3File mp3file = null;
        try {
            file = convert(bytes);
            mp3file = new Mp3File(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ID3v2 info = new ID3v23Tag();
        info.setTitle(mp3file.getId3v2Tag().getTitle());
        info.setArtist(mp3file.getId3v2Tag().getArtist());

        file.delete();
        return info;
    }

    public long getLength(byte[] bytes) {
        File file = null;
        Mp3File mp3file = null;
        try {
            file = convert(bytes);
            mp3file = new Mp3File(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return mp3file.getLengthInSeconds();

    }

    private synchronized File convert(byte[] file) throws Exception
    {
        File convFile = new File("temp");
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file);
        fos.close();
        return convFile;
    }
}
