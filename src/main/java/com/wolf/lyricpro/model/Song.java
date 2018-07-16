package com.wolf.lyricpro.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "nvarchar(255)")
    private String name;
    @NotNull
    @Column(columnDefinition = "nvarchar(255)")
    private String Artist;
    @NotNull
    private String url;
    @NotNull
    private int crawlCode;

    public Song() {
    }

    public Song(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCrawlCode() {
        return crawlCode;
    }

    public void setCrawlCode(int crawlCode) {
        this.crawlCode = crawlCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
