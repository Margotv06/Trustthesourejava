package controller;

/**
 * Created by pjvan on 30-11-2015.
 */
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TweetGrabber
{
    public static void TweetGrabber(String url){
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element tweetText = doc.select("p.js-tweet-text.tweet-text").first();
        System.out.println(tweetText.text());

    }

}