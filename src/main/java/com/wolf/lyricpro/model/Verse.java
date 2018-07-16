package com.wolf.lyricpro.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_verse")
public class Verse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private int sequence;

    @NotNull
    @Column(columnDefinition = "nvarchar(MAX)")
    private String verse;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    public Verse() {
    }

    public Verse(@NotNull String verse) {
        this.verse = verse;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
