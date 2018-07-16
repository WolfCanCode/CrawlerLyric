package com.wolf.lyricpro.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "lyric")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lyric", propOrder = {
        "verses"
})
public class LyricXML {
    private List<VerseXML> verses;

    public List<VerseXML> getVerses() {
        return verses;
    }

    public void setVerses(List<VerseXML> verses) {
        this.verses = verses;
    }

    @Override
    public String toString() {
        return "ClassPojo [verses = " + verses + "]";
    }
}