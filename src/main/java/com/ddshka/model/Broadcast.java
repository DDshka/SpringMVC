package com.ddshka.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "currentbroadcasts", schema = "mydb", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
public class Broadcast {
    private int id;
    private String songName;
    private User user;

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
    @Column(name = "songName")
    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Broadcast broadcast = (Broadcast) o;

        if (id != broadcast.id) return false;
        if (songName != null ? !songName.equals(broadcast.songName) : broadcast.songName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (songName != null ? songName.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "broadcaster_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
