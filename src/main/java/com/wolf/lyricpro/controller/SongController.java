package com.wolf.lyricpro.controller;


import com.wolf.lyricpro.model.*;
import com.wolf.lyricpro.repository.SongRepository;
import com.wolf.lyricpro.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SongController {

    @Autowired
    SongRepository songRepository;
    @Autowired
    VerseRepository verseRepository;


    @RequestMapping(value = "/song/{songId}", produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.GET)
    public SongXML getSongBySongID(@PathVariable(value = "songId") Integer songID) {
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
        return songXML;
    }

    @RequestMapping(value = "song/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ListSongXML getSongBySongName(@RequestParam("q") String name) {
        List<Song> songList = songRepository.findByName(name);
        List<SongXML> songXMLList = new ArrayList<>();
        LyricXML lyric = new LyricXML();
        for (int i = 0; i < songList.size(); i++) {
            Song song = songList.get(i);
            SongXML songXML = new SongXML();
            List<Verse> verseList = verseRepository.findAllBySong(song);
            List<VerseXML> verseXMLList = new ArrayList<>();
            verseList.forEach(verse -> {
                verseXMLList.add(new VerseXML(verse.getSequence(), verse.getVerse()));
            });
            songXML.setId(song.getId());
            songXML.setName(song.getName());
            songXML.setArtist(song.getArtist());
            lyric.setVerses(verseXMLList);
            songXML.setLyricXML(lyric);
            songXMLList.add(songXML);
        }
        ListSongXML listSongXML = new ListSongXML(songXMLList);
        return listSongXML;
    }
}
