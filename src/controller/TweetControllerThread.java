package controller;
import model.Tweet;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.*;


/**
 *    ______                __  __  __        _____
 *   /_  __/______  _______/ /_/ /_/ /_  ___ / ___/____  __  _______________
 *    / / / ___/ / / / ___/ __/ __/ __ \/ _ \\__ \/ __ \/ / / / ___/ ___/ _ \
 *   / / / /  / /_/ (__  ) /_/ /_/ / / /  __/__/ / /_/ / /_/ / /  / /__/  __/
 *  /_/ /_/   \__,_/____/\__/\__/_/ /_/\___/____/\____/\__,_/_/   \___/\___/
 *
 *  This is the Class that takes tweets of the List, processes them and sends them back to the web
 *
 *
 */

/**
 * Created by daant on 11-Dec-15.
 */
public class TweetControllerThread implements Runnable {
    private int tweetGathered;
    private LinkedList<JSONObject> documents;
    private int tweetGathered2;
    private TweetController tweetController;
    private int tweetsToGather;

    public TweetControllerThread(LinkedList<JSONObject> documents, TweetController tweetController) {
        this.documents = documents;
        this.tweetController = tweetController;
        tweetsToGather = 0;
        tweetGathered = 1;
        tweetGathered2 = 2;
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
        Document doc = new Document("");
        try {

            JSONObject jdoc =  documents.pop();
            String html_content = jdoc.get("items_html").toString();
            doc = Jsoup.parse(html_content);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        // loop over every tweet in the DOM
        for(int i =0 ; i< doc.select(".js-tweet-text.tweet-text").size();i++) {
            // make a tweet.java instance from the tweet
            Tweet tweet = new Tweet( doc.select(".js-tweet-text.tweet-text").get(i).text(), doc, i);
            // add it tot he ArrayList
            tweetController.addTweet(tweet);
            waiting(50);
            tweetController.sendTweet(tweet, "tweet");

        }
        // Sends a string back to the terminal of the web
        System.out.println("Amount of tweets gathered: "+tweetController.getTweetsSize());
        if (tweetGathered == tweetController.getTweetsSize()) {
            if (tweetGathered2 == tweetGathered2) {
                //System.out.println("Tweet gathering has closed because the crawler cant find anymore tweets");
                //tweetController.closeSession("Tweet gathering has closed because the crawler cant find anymore tweets");
            }
        }
        if (tweetGathered != 0) {
            tweetGathered2 = tweetGathered;
        }
        tweetGathered = tweetController.getTweetsSize();
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
            int amountOfTweets = tweetController.getTweetsSize();
            if (amountOfTweets > tweetsToGather) {
                System.out.println(tweetController.getTweetsSize());
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
