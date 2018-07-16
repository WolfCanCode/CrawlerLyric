package com.wolf.lyricpro.crawler;

import com.wolf.lyricpro.model.Song;
import com.wolf.lyricpro.model.Verse;
import com.wolf.lyricpro.repository.SongRepository;
import com.wolf.lyricpro.utils.XmlUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LyricsVNCrawler extends MainCrawler {

    private final String lyricVn = "http://www.lyrics.vn/lyrics/topview/";
    SongRepository songRepository;

    @Override
    public void parse(List listSong) throws IOException {
        System.out.println("=======START CRAWL LYRICS VN=======");
        crawlSong(listSong);
        System.out.println("=======END CRAWL LYRICS VN=======");
        System.out.println();
    }

    public void crawlSong(List listSong) throws IOException {
        String url = lyricVn;
        Song song = null;
        int count = 1;
        for (int i = 1; i <= 20; i++) {
            if (count % 1000 == 0) {
                System.out.println("You have been crawled " + count + " songs,,,,");
            }
            String u = "";
            u = url + i + ".html";
            InputStream is = null;
            is = converHtmlToIS(u);
            InputStream checkedIs = checkAndCleanTag(is);
            XMLStreamReader xmlReader = null;
            boolean found = false;
            boolean foundFirstATag = false;

            try {
                xmlReader = XmlUtils.parseFileToStaxCursor(checkedIs);
                while (xmlReader.hasNext()) {
                    if (!found) {
                        song = new Song();
                    }
                    try {
                        int cursor = xmlReader.next();
                        if (cursor == XMLStreamConstants.START_ELEMENT) {
                            String tagName = xmlReader.getLocalName();
                            if ("div".equals(tagName)) {
                                String className = XmlUtils.getNodeStaxValue(xmlReader, "div", "", "class");
                                if (className.equals("item shadow border-bot-tt")) {
                                    found = true;
                                }
                            }

                            if ("a".equals(tagName) && found && foundFirstATag) {
                                String artist = xmlReader.getElementText();
                                song.setArtist(artist);
                                song.setCrawlCode(2);
                                listSong.add(song);
                                count++;
                                found = false;
                                System.out.println(song.getName() + " doned.");
                                foundFirstATag = false;
                            }

                            if ("a".equals(tagName) && found && !foundFirstATag) {
                                System.out.print(count + " > ");
                                String songUrl = XmlUtils.getNodeStaxValue(xmlReader, "a", "", "href");
                                song.setUrl(songUrl);
                                System.out.println(songUrl);
                                String songName = xmlReader.getElementText();
                                song.setName(songName);
                                foundFirstATag = true;
                            }

                        }
                    } catch (NullPointerException e) {
                        Logger.getLogger(LyricsVNCrawler.class.getName()).log(Level.SEVERE, null, e);
                        break;
                    }
                }
                System.out.println("Total " + count + " links");
            } catch (XMLStreamException e) {
                System.out.println("Skip this song cause of well-form");
            } catch (Exception e) {
                Logger.getLogger(LyricsVNCrawler.class.getName()).log(Level.SEVERE, null, e);
            }

        }

    }

    @Override
    public void crawlLyric(Song song, List<Verse> verses) throws IOException {
        InputStream lyricIs = converHtmlToIS(song.getUrl());
        InputStream checkLyricIs = checkAndCleanTagLyric(lyricIs);
        XMLStreamReader xmlReaderLyric = null;
        int seq = 1;
        try {
            xmlReaderLyric = XmlUtils.parseFileToStaxCursor(checkLyricIs);
            while (xmlReaderLyric.hasNext()) {
                try {
                    int cursorLyric = xmlReaderLyric.next();
                    if (cursorLyric == XMLStreamConstants.START_ELEMENT) {
                        String tagNameLyric = xmlReaderLyric.getLocalName();
                        if ("div".equals(tagNameLyric)) {
                            String classNameLyric = XmlUtils.getNodeStaxValue(xmlReaderLyric, "div", "", "class");
                            if (classNameLyric.equals("detail")) {
                                String lyric = xmlReaderLyric.getElementText();
                                String[] versesArray = lyric.split("borick");
                                for (int i = 0; i < versesArray.length; i++) {
                                    Verse verse = new Verse();
                                    verse.setSequence(seq);
                                    verse.setVerse(versesArray[i].trim());
                                    verse.setSong(song);
                                    verses.add(verse);
                                    seq++;
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    break;
                }
            }

//            System.out.println(song.getName() + " doned.");
        } catch (XMLStreamException e) {
            System.out.println("Skip this song cause of wellform");
        } catch (Exception e) {
            Logger.getLogger(LyricsVNCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private InputStream checkAndCleanTag(InputStream is)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        if (is != null) {
            BufferedReader re = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            String str;
            while ((str = re.readLine()) != null) {
                String line = str.replaceAll("&nbsp;", " ");
                line = line.replaceAll("&", "");
                line = line.replaceAll("\n|\r", "");
                line = line.replaceAll("<br>", "");
                builder.append(line);
            }
            clearTrashHtml("<div class=\"site-view idx-list-item clearfix\">",
                    "</div></div></div>", builder);
            builder.append("</div></div></div>");
            return convertSBToIS(builder);
        }
        return null;
    }

    private InputStream checkAndCleanTagLyric(InputStream is)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        if (is != null) {
            BufferedReader re = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            String str;
            while ((str = re.readLine()) != null) {
                String line = str.replaceAll("&nbsp;", " ");
                line = line.replaceAll("&", "");
                line = line.replaceAll("\n", "");
                line = line.replaceAll("<br/>", "borick");
                builder.append(line);

            }

            clearTrashHtml("<div class=\"detail\">",
                    "</div>", builder);
            return convertSBToIS(builder);
        }
        return null;
    }
}
