package com.wolf.lyricpro.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VerseXML {

    @XmlAttribute(name = "sequence", required = true)
    private int sequence;
    @XmlValue
    private String verse;

    public VerseXML() {
    }

    public VerseXML(int sequence, String verse) {
        this.sequence = sequence;
        this.verse = verse;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }
}
