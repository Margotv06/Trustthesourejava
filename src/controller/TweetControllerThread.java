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
    private int tweetsToGather;

    public TweetControllerThread(LinkedList<Document> documents, TweetController tweetController) {
        this.documents = documents;
        this.tweets = new ArrayList<>();
        this.tweetController = tweetController;
        tweetsToGather = 0;
    }
    @Override
    public void run() {
        while (documents.isEmpty()) {
            waiting(100);
        }
        //tweetController.sendMessage("Tweet collection has started");
        while (!Thread.currentThread().isInterrupted()) {
            // checks to see if there is a document waiting
            if (documents.isEmpty() == false) {
                handleDoc();
                checkCount();
            } else {
                // Waits before trying to see if there is another DOM waiting
                // Can be interrupted
                waiting(100);
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
            // make a tweet.java instance from the tweet
            Tweet tweet = new Tweet( doc.select(".js-tweet-text.tweet-text").get(i).text(), doc, i);
            // add it tot he ArrayList
            tweets.add(tweet);
            waiting(50);
            tweetController.sendMessage(tweet.getMessage(), "tweet");

        }
        // Sends a string back to the terminal of the web
        System.out.println("Amount of tweets gathered: "+tweets.size());
    }
    /*
    waits
     */
    synchronized private void waiting(int nanoseconds) {
        try {
            this.wait(nanoseconds);
        }


        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return;
    }
    /*
    checks there are enough tweets gathered
     */
    private void checkCount(){
        if (tweetsToGather != 0) {
            int amountOfTweets = tweets.size();
            if (amountOfTweets > tweetsToGather) {
                System.out.println(tweets.size());
                tweetController.closeSession();
            }
        }
    }
    /*
    sets limit to tweets to gather
     */
    public void setLimit(int tweetsToGather){
        this.tweetsToGather = tweetsToGather;
    }
}
