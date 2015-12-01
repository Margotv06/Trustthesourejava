package controller;

/**
 * Created by pjvan on 30-11-2015.
 */
import java.io.*;

import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TweetGrabber
{
    public static void TweetGrabber(String url, Session session, String key){
        Document doc = null;
        key = keyTable(key);

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element tweetText = doc.select(key).first();
        System.out.println(tweetText.text());
        try{
            session.getRemote().sendString(tweetText.text());
        }
        catch (IOException e){
            System.err.println(e);
        }

    }

    private static String keyTable(String key){
        switch (key){
            case "tweet":
                return "p.js-tweet-text.tweet-text";
            case "location":
                return "ProfileHeaderCard-locationText";
            case "registered":
                return "ProfileHeaderCard-joinDateText";
            case "description":
                return "ProfileHeaderCard-bio";
            case "profilename":
                return "ProfileHeaderCard-nameLink";
            /*case "":
                break;
            case "":
                break;
            case "":
                break;
            case "":
                break;
            case "":
                break;
            case "":
                break;
            case "":
                break;*/
        }
        return key;
    }

}