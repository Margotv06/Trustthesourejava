package controller;

import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by pjvan on 8-12-2015.
 */
public class TweetController {
    private ArrayList<Tweet> tweets;
    private HashMap<String,ArrayList<Tweet>> keywords;
    private Thread tweetGrabber;
    private JSONObject message;

    public TweetController(){
            message = new JSONObject();
            tweetGrabber = new Thread(new TweetGrabber2(message));
            tweetGrabber.start();

    }

    public synchronized void sendCommand(JSONObject command){
        System.out.println("Got Command");
        message = command;
    }
}
