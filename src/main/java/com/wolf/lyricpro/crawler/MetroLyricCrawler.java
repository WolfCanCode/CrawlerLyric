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

public class MetroLyricCrawler extends MainCrawler {

    private final String azURL = "http://www.metrolyrics.com/";
    SongRepository songRepository;

    @Override
    public void parse(List listSong) throws IOException {
        System.out.println("=======START CRAWL METRO LYRIC=======");
        crawlSong(listSong);
        System.out.println("=======END CRAWL METRO LYRIC=======");
        System.out.println();
    }

    public void crawlSong(List listSong) throws IOException {
        String url = azURL;
        Song song = null;
        int count = 0;
        for (int i = 5; i > 0; i--) {
            String u = "";
            if (i == 5) {
                u = url + "rolling-stone-top500.html";
            } else {
                u = url + "rolling-stone-top500-" + i + ".html";
            }
            InputStream is = null;
            is = converHtmlToIS(u);
            InputStream checkedIs = checkAndCleanTag(is);
            XMLStreamReader xmlReader = null;
            boolean found = false;
            boolean foundFirstATag = false;
            boolean foundSecondATag = false;

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
                            if ("li".equals(tagName)) {
                                found = true;
                            }
                            if ("span".equals(tagName) && found) {
                                String className = XmlUtils.getNodeStaxValue(xmlReader, tagName, "", "class");
                                if (className.equals("num")) {
                                    System.out.print(xmlReader.getElementText() + " > ");
                                }

                            }
                            if ("a".equals(tagName) && found) {
                                String className = XmlUtils.getNodeStaxValue(xmlReader, tagName, "", "class");

                                if (className.equals("title hasvidtoplyriclist") || className.equals("title ")) {
                                    String songUrl = XmlUtils.getNodeStaxValue(xmlReader, "a", "", "href");
                                    String songName = xmlReader.getElementText().split("Lyrics")[0];
                                    song.setName(songName);
                                    song.setUrl(songUrl);
                                }

                                if (className.equals("subtitle")) {
                                    String artist = xmlReader.getElementText();
                                    song.setArtist(artist);
                                    song.setCrawlCode(1);
                                    listSong.add(song);
                                    count++;
                                    found = false;
                                    System.out.println(song.getName() + " doned.");
                                }


                            }


                        }
                    } catch (NullPointerException e) {
                        Logger.getLogger(MetroLyricCrawler.class.getName()).log(Level.SEVERE, null, e);
                        break;
                    }
                }

                System.out.println("Total " + count + " links");
            } catch (Exception e) {
                Logger.getLogger(MetroLyricCrawler.class.getName()).log(Level.SEVERE, null, e);
            }

        }

    }

    @Override
    public boolean crawlLyric(Song song, List<Verse> verses) throws IOException {
        InputStream lyricIs = converHtmlToIS(song.getUrl());
        InputStream checkLyricIs = checkAndCleanTagLyric(lyricIs);
        XMLStreamReader xmlReaderLyric = null;
        boolean isValid = false;
        int seq = 1;
        try {
            xmlReaderLyric = XmlUtils.parseFileToStaxCursor(checkLyricIs);
            while (xmlReaderLyric.hasNext()) {
                try {
                    int cursorLyric = xmlReaderLyric.next();
                    if (cursorLyric == XMLStreamConstants.START_ELEMENT) {
                        String tagNameLyric = xmlReaderLyric.getLocalName();
                        if ("p".equals(tagNameLyric)) {
                            String classNameLyric = XmlUtils.getNodeStaxValue(xmlReaderLyric, "p", "", "class");
                            if (classNameLyric.equals("verse")) {
                                String insideVerse = xmlReaderLyric.getElementText();
                                String[] versesArray = insideVerse.split("borick");
                                for (int i = 0; i < versesArray.length; i++) {
                                    Verse verse = new Verse();
                                    verse.setVerse(versesArray[i].trim());
                                    verse.setSequence(seq);
                                    verse.setSong(song);
                                    verses.add(verse);
                                    seq++;
                                }
                                isValid = true;
                            }
                        }
                        if ("div".equals(tagNameLyric)) {
                            String classNameLyric = XmlUtils.getNodeStaxValue(xmlReaderLyric, "div", "", "class");
                            if (classNameLyric.equals("bottom-mpu")) {
                                break;
                            }
                        }
                    }

                } catch (XMLStreamException e) {
                    return false;
                } catch (NullPointerException e) {
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MetroLyricCrawler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            return isValid;
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
                line = line.replaceAll("<br>", "");
                builder.append(line);
            }
            clearTrashHtml("<div class=\"song-list content clearfix\">",
                    "</div></div><div class=\"grid_4\">", builder);
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
                line = line.replaceAll("<br>", "borick");
                builder.append(line);

            }
            clearTrashHtml("<div class=\"lyrics-body\">",
                    "</div>", builder);
            return convertSBToIS(builder);
        }
        return null;
    }
}
