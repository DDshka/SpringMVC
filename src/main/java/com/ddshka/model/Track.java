package com.ddshka.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "tracks", schema = "mydb", catalog = "")
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private int id;
    private byte[] file;
    private Timestamp timestamp;
    private User user;
    private Integer position;

    private String artist;
    private String composer;
    private String title;
    private String album;
    private byte[] albumImg;

    @Id
    @Column(name = "id")
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "file")
    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Basic
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    @Basic
    @Column(name = "position")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Basic
    @Column(name = "artist", nullable = true, length = 45)
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Basic
    @Column(name = "composer", nullable = true, length = 45)
    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "album", nullable = true, length = 45)
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Basic
    @Column(name = "album_img", nullable = true)
    public byte[] getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(byte[] albumImg) {
        this.albumImg = albumImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (id != track.id) return false;
        if (!Arrays.equals(file, track.file)) return false;
        if (timestamp != null ? !timestamp.equals(track.timestamp) : track.timestamp != null) return false;
        if (position != null ? !position.equals(track.position) : track.position != null) return false;
        if (artist != null ? !artist.equals(track.artist) : track.artist != null) return false;
        if (composer != null ? !composer.equals(track.composer) : track.composer != null) return false;
        if (title != null ? !title.equals(track.title) : track.title != null) return false;
        if (album != null ? !album.equals(track.album) : track.album != null) return false;
        if (!Arrays.equals(albumImg, track.albumImg)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Arrays.hashCode(file);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (composer != null ? composer.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(albumImg);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrackInfo toTrackInfo() {
        TrackInfo trackInfo = new TrackInfo();
            trackInfo.setId(id);
            trackInfo.setUser(user);
            trackInfo.setPosition(position);
            trackInfo.setTimestamp(timestamp);
            trackInfo.setArtist(artist);
            trackInfo.setComposer(composer);
            trackInfo.setTitle(title);
            trackInfo.setAlbum(album);
            trackInfo.setAlbumImg(albumImg);

        return trackInfo;
    }
}
