package com.wolf.lyricpro.controller;

import com.wolf.lyricpro.model.*;
import com.wolf.lyricpro.repository.SongRepository;
import com.wolf.lyricpro.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ViewController {

    @Autowired
    SongRepository songRepository;
    @Autowired
    VerseRepository verseRepository;

    @GetMapping("/song")
    public String getSongView() {
        return "song";
    }

    @RequestMapping(value = "/song/{songId}", method = RequestMethod.GET)
    public String getSongBySongID(Model model, @PathVariable(value = "songId") Integer songID) {
        SongXML songXML = new SongXML();
        Song song = songRepository.findById(songID);
        List<Verse> verseList = verseRepository.findAllBySong(song);
        List<VerseXML> verseXMLList = new ArrayList<>();
        LyricXML lyric = new LyricXML();
        verseList.forEach(verse -> {
            verseXMLList.add(new VerseXML(verse.getSequence(), verse.getVerse()));
        });
        songXML.setId(song.getId());
        songXML.setName(song.getName());
        songXML.setArtist(song.getArtist());
        lyric.setVerses(verseXMLList);
        songXML.setLyricXML(lyric);
        try {
            System.out.println(songXML.getName());
            JAXBContext ctx = JAXBContext.newInstance(songXML.getClass());
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            marshaller.marshal(songXML, sw);
            model.addAttribute("songXML", sw.toString());
        } catch (JAXBException e) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, e);
        }

        return "songById";
    }
}
