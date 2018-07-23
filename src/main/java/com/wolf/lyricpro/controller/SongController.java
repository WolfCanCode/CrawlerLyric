package com.wolf.lyricpro.controller;


import com.wolf.lyricpro.model.*;
import com.wolf.lyricpro.repository.SongRepository;
import com.wolf.lyricpro.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SongController {

    @Autowired
    SongRepository songRepository;
    @Autowired
    VerseRepository verseRepository;


    @RequestMapping(value = "song/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ListSongXML getSongBySongName(@RequestParam("q") String keyword) {
        String[] keywords = keyword.split(" ");
        List<Song> songList = new ArrayList<>();
        List<Song> songListTmp = new ArrayList<>();
        for (int k = 0; k < keywords.length; k++) {
            String searchKey = "";
            for (int j = 0; j <= k; j++) {
                searchKey += keywords[j] + " ";
            }
            if (k == 0) {
                songList = songRepository.findByName(searchKey);
                songListTmp = verseRepository.findSongByVerse(searchKey);
                for (int i = 0; i < songListTmp.size(); i++) {
                    if (isNotExisted(songList, songListTmp.get(i).getId(), false)) {
                        songList.add(songListTmp.get(i));
                    }
                }
            } else {
                songListTmp = songRepository.findByName(searchKey);
                for (int i = 0; i < songListTmp.size(); i++) {
                    if (isNotExisted(songList, songListTmp.get(i).getId(), true)) {
                        songList.add(0, songListTmp.get(i));
                    } else {
                        songList.add(0, songListTmp.get(i));
                    }
                }
                System.err.println(searchKey);
                songListTmp = verseRepository.findSongByVerse(searchKey);
                for (int j = 0; j < songListTmp.size(); j++) {
                    if (isNotExisted(songList, songListTmp.get(j).getId(), true)) {
                        songList.add(0, songListTmp.get(j));
                    } else {
                        songList.add(0, songListTmp.get(j));
                    }
                }
            }
        }
        List<SongXML> songXMLList = new ArrayList<>();
        for (int i = 0; i < songList.size(); i++) {
            Song song = songList.get(i);
            SongXML songXML = new SongXML();
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
            songXMLList.add(songXML);
        }
        ListSongXML listSongXML = new ListSongXML(songXMLList);
        return listSongXML;
    }


    private boolean isNotExisted(List<Song> list, int id, boolean isDelete) {
        for (Song s : list) {
            if (s.getId() == id) {
                if (isDelete) {
                    list.remove(s);
                }
                return false;
            }
        }
        return true;
    }
}
