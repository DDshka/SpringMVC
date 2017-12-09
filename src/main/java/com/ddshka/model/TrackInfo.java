package com.ddshka.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "tracks", schema = "mydb", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
public class TrackInfo {
    private int id;
    private Timestamp timestamp;
    private Integer position;
    private String artist;
    private String composer;
    private String title;
    private String album;
    private byte[] albumImg;
    private User user;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "timestamp", nullable = false)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "position", nullable = false)
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

        TrackInfo trackInfo = (TrackInfo) o;

        if (id != trackInfo.id) return false;
        if (position != trackInfo.position) return false;
        if (timestamp != null ? !timestamp.equals(trackInfo.timestamp) : trackInfo.timestamp != null) return false;
        if (artist != null ? !artist.equals(trackInfo.artist) : trackInfo.artist != null) return false;
        if (composer != null ? !composer.equals(trackInfo.composer) : trackInfo.composer != null) return false;
        if (title != null ? !title.equals(trackInfo.title) : trackInfo.title != null) return false;
        if (album != null ? !album.equals(trackInfo.album) : trackInfo.album != null) return false;
        if (!Arrays.equals(albumImg, trackInfo.albumImg)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + position;
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (composer != null ? composer.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(albumImg);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
