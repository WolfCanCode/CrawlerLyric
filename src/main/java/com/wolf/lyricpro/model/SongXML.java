package com.wolf.lyricpro.model;


import javax.xml.bind.annotation.*;

@XmlRootElement(name = "song")
@XmlSeeAlso(LyricXML.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "song", propOrder = {
        "name",
        "artist",
        "lyricXML"
})
public class SongXML {
    @XmlAttribute
    private int Id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "artist", required = true)
    private String artist;
    @XmlElement(name = "lyric", required = true)
    private LyricXML lyricXML;

    public SongXML() {
    }

    public SongXML(String name, String artist, LyricXML lyricXML) {
        this.name = name;
        this.artist = artist;
        this.lyricXML = lyricXML;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LyricXML getLyricXML() {
        return lyricXML;
    }

    public void setLyricXML(LyricXML lyricXML) {
        this.lyricXML = lyricXML;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
