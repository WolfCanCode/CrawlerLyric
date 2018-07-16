package com.wolf.lyricpro.controller;

import com.wolf.lyricpro.crawler.LyricsVNCrawler;
import com.wolf.lyricpro.crawler.MainCrawler;
import com.wolf.lyricpro.crawler.MetroLyricCrawler;
import com.wolf.lyricpro.model.Song;
import com.wolf.lyricpro.model.Verse;
import com.wolf.lyricpro.repository.SongRepository;
import com.wolf.lyricpro.repository.VerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlController {
    @Autowired
    SongRepository songRepository;
    @Autowired
    VerseRepository verseRepository;

    @GetMapping("/crawlAll")
    public void startCrawl() throws IOException {
        List<Song> SongList = new ArrayList<>();
        MainCrawler metroCrawl = new MetroLyricCrawler();
        MainCrawler lyricVnCrawl = new LyricsVNCrawler();
        metroCrawl.parse(SongList);
        lyricVnCrawl.parse(SongList);
        songRepository.saveAll(SongList);
        System.out.println("=======ALL SONGS HAVE BEEN CRAWLED=======");
        System.out.println();
        System.out.println("=======START CRAWL LYRIC=======");
        List<Song> listSong = songRepository.findAll();
        List<Verse> listVerse = new ArrayList<>();
        for (int i = 0; i < listSong.size(); i++) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (listSong.size() % i == 0) {
                System.out.println((float) i / listSong.size() * 100 + "%");
            }
            if (listSong.get(i).getCrawlCode() == 1) {
                metroCrawl.crawlLyric(listSong.get(i), listVerse);
            } else {
                lyricVnCrawl.crawlLyric(listSong.get(i), listVerse);
            }
        }
        System.out.println(listVerse.size());
        verseRepository.saveAll(listVerse);
        System.out.println("=======END CRAWL LYRIC=======");

    }
}
