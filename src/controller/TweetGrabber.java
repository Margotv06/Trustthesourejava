package controller;

/**
 * Created by pjvan on 30-11-2015.
 */
import java.io.*;
import java.util.ArrayList;

import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class TweetGrabber{
    private static ArrayList<Tweet> tweets = new ArrayList<Tweet>();


    public static void TweetGrabber(String url, Session session){
        new Tweet().Tweet(url,session);
    }

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
            // Returns the actual content of the tweet
            case "tweet":
                return "p.js-tweet-text.tweet-text";
            // Returns the profilename
            case "profilename":
                return "strong.js-action-profile-name";
            // Returns the username
            case "username":
                return "span.js-action-profile-name";
            // Returns the amount of retweets (in String)
            // example: "Retweets: (number of RT)"
            case "retweets":
                return "li.js-stat-retweets";
            // Returns the amount of likes/favorites (in String)
            // example: "Retweets: (number of RT)"
            case "likes":
            case "favorites":
                return "li.js-stat-favorites";
            // example: "Retweets: (number of RT)"
            case "time":
                return "span.metadata";
        }
        return key;
    }

}