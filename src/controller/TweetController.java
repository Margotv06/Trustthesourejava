package controller;

import model.IgnoreList;
import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.*;


/**
 * Created by pjvan on 8-12-2015.
 */
public class TweetController {
    private ArrayList<Tweet> tweets;
    private HashMap<String,ArrayList<Tweet>> keywords;
    private Thread tweetGrabber;
    private Thread tweetControllerThread;
    private Session session;
    //private List<JSONObject> grabberCommand = Collections.synchronizedList(new LinkedList<JSONObject>());
    private Stack<JSONObject> grabberCommand = new Stack<>();
    private LinkedList<Document> documents;

    public TweetController() {

        documents = new LinkedList<Document>();
        tweetGrabber = new Thread(new TweetGrabber2(grabberCommand, documents));
        tweetGrabber.start();
        tweetControllerThread = new Thread(new TweetControllerThread(documents, this));
        tweetControllerThread.start();
    }
    public void setSession(Session session) {
        this.session = session;
    }

    public synchronized void sendCommand(JSONObject command){
        System.out.println("Got Command");
        grabberCommand.add(command);
    }
    /*
    Method for sending a string back to the web
     */
    public boolean sendMessage(String message) {

        // sends the twitter message back to the web
        try{
            session.getRemote().sendString(message);
        }
        catch (IOException e){
            System.err.println(e);
            return false;
        }
        return true;
    }
}
