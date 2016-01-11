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
    private TweetControllerThread controllerClass;
    private Session session;
    //private List<JSONObject> grabberCommand = Collections.synchronizedList(new LinkedList<JSONObject>());
    private Stack<JSONObject> grabberCommand = new Stack<>();
    private LinkedList<Document> documents;

    public TweetController() {
        documents = new LinkedList<Document>();
        tweetGrabber = new Thread(new TweetGrabber2(grabberCommand, documents, this));
        tweetGrabber.start();


        controllerClass = new TweetControllerThread(documents, this);

        tweetControllerThread = new Thread(controllerClass);
        tweetControllerThread.start();
    }
    public void setSession(Session session) {
        this.session = session;
    }

    /*
    The one command to rule them all.
     */
    public void closeSession(String message){
        sendMessage(message, "message");
        tweetControllerThread.interrupt();
        tweetGrabber.interrupt();
    }
    public void closeSession() {
        closeSession("Tweet gathering has stopped");
    }

    public synchronized void sendCommand(JSONObject command, int tweetsToGather){
        System.out.println("Got Command");
        grabberCommand.add(command);
        controllerClass.setLimit(tweetsToGather);
    }
    /*
    Method for sending a string back to the web
     */
    public boolean sendMessage(String message, String kind) {

        // sends the twitter message back to the web
        try{


            String json = "{/MSG/: /"+kind+"/, /VALUE/: /"+message+"/}";
            json = json.replace('/', '"');
            session.getRemote().sendString(json);
        }
        catch (IOException e){
            System.err.println(e);
            return false;
        }
        return true;
    }
    /*
    method for manipulating the tweet ArrayList in TweetControllerThread.
    Gets called from the web with an array of words to delete
     */
    public void updateTweetList(String[] words) {
        //sendMessage("got inside the update method");
        for (String word: words) {
            //sendMessage(word);
        }
    }
    /*
    checks if session is set
     */
    public boolean checkSession() {
        if (this.session == null) {
            return false;
        }
        else {
            return true;
        }
    }
}
