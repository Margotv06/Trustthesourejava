package controller;
import model.IgnoreList;
import model.Tweet;
import org.json.JSONObject;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by daant on 11-Dec-15.
 */
public class TweetControllerThread implements Runnable {
    private LinkedList<Document> documents;
    private ArrayList<Tweet> tweets;

    public TweetControllerThread(LinkedList<Document> documents) {
        this.documents = documents;
        this.tweets = new ArrayList<Tweet>();
    }
    @Override
    public void run() {

        while (true) {
            // checks to see if there is a document waiting
            if (documents.isEmpty() == false) {
                handleDoc();
            } else {
                // Waits before trying to see if there is another DOM waiting
                // Can be interrupted
                waiting();
            }
        }
    }
    /*
    Handles the incoming DOM
     */
    private void handleDoc() {
        Document doc = documents.pop();
        // loop over every tweet in the DOM
        for(int i =0 ; i< doc.select(".js-tweet-text.tweet-text").size();i++) {
            // make a tweet.java instancce from the tweet
            Tweet tweet = new Tweet( doc.select(".js-tweet-text.tweet-text").get(i).text(), doc, i);
            // add it tot he ArrayList
            tweets.add(tweet);
            System.out.println("Original tweet message: "+tweet.getMessage());
            System.out.println("Keywords  message: "+tweet.getMessageWords());
        }
        //System.out.println("Tweet array size: "+tweets.size());
    }
    /*
    waits
     */
    synchronized private void waiting() {
        try {
            this.wait(100);
        }
        catch (InterruptedException e) {

        }
        return;
    }
}
