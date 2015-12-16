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
    private TweetController tweetController;

    public TweetControllerThread(LinkedList<Document> documents, TweetController tweetController) {
        this.documents = documents;
        this.tweets = new ArrayList<Tweet>();
        this.tweetController = tweetController;
    }
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            // checks to see if there is a document waiting
            if (documents.isEmpty() == false) {
                handleDoc();
            } else {
                // Waits before trying to see if there is another DOM waiting
                // Can be interrupted
                waiting();
            }
        }
        System.out.println("thread is interuptted");
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

        }
        // Sends a string back to the terminal of the web
        tweetController.sendMessage("Amount of tweets gathered: "+tweets.size());
    }
    /*
    waits
     */
    synchronized private void waiting() {
        try {
            this.wait(100);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return;
    }
}
