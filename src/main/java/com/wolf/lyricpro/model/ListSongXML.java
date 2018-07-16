package com.wolf.lyricpro.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "songs", propOrder = {
        "songs"
})
public class ListSongXML {
    @XmlElement
    private List<SongXML> songs = new ArrayList<>();

    public ListSongXML() {
    }

    public ListSongXML(List<SongXML> songs) {
        this.songs = songs;
    }

    public List<SongXML> getListSong() {
        return songs;
    }

    public void setListSong(List<SongXML> songs) {
        this.songs = songs;
    }
}
