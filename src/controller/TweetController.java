package controller;

import model.Tweet;
import org.json.JSONObject;

import java.util.*;


/**
 * Created by pjvan on 8-12-2015.
 */
public class TweetController {
    private ArrayList<Tweet> tweets;
    private HashMap<String,ArrayList<Tweet>> keywords;
    private Thread tweetGrabber;
    //private List<JSONObject> grabberCommand = Collections.synchronizedList(new LinkedList<JSONObject>());
    private Stack<JSONObject> grabberCommand = new Stack<>();

    public TweetController(){

            tweetGrabber = new Thread(new TweetGrabber2(grabberCommand));
            tweetGrabber.start();

    }

    public synchronized void sendCommand(JSONObject command){
        System.out.println("Got Command");
        grabberCommand.add(command);
    }
}
