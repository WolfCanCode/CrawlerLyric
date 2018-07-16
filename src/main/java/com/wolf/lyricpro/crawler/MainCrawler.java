package com.wolf.lyricpro.crawler;

import com.wolf.lyricpro.model.Song;
import com.wolf.lyricpro.model.Verse;
import com.wolf.lyricpro.utils.XmlUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MainCrawler {


    protected static InputStream converHtmlToIS(String uri) {

        try {
            URL url = new URL(uri);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.addRequestProperty("User-Agent", "Mozilla/4.76");
            InputStream is = co.getInputStream();
            return is;
        } catch (MalformedURLException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public abstract void parse(List listSong) throws IOException;

    public abstract void crawlLyric(Song song, List<Verse> verses) throws IOException;

    protected InputStream convertSBToIS(StringBuilder builder)
            throws UnsupportedEncodingException {

        return new ByteArrayInputStream(
                builder.toString().getBytes(StandardCharsets.UTF_8.name()));
    }

    protected void clearTrashHtml(String start, String end, StringBuilder builder) {
        String st = start;
        String en = end;
        int startIndex = builder.indexOf(st);
        builder.delete(0, startIndex);
        int endIndex = builder.indexOf(en);
        builder.delete(endIndex + 6, builder.length());
    }
}
