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

@Deprecated
public class TweetGrabber{
    private static ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    @Deprecated
    public static void TweetGrabber(String url, Session session){

        // Getting the page
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Getting the tweet
        Element tweetText = getInfo("tweet", doc);

        // Making a tweet object with the text
        //Tweet tweet = new Tweet(tweetText.text(), doc);

        // let's send the message back to the web
        //sendMessage(tweet.getMessage(), session);

    }





    /*
    Method for getting information from the DOM
     */
    @Deprecated
    public static Element getInfo(String key, Document doc){

        return doc.select(keyTable(key)).first();
    }

    @Deprecated
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

            // Returns the time the tweet was send
            case "time":
                return "span.metadata";
        }
        return key;
    }

}