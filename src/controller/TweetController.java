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
    private ProfileGrabber profileGrabber;
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
        tweets = new ArrayList<Tweet>();
        documents = new LinkedList<Document>();



        controllerClass = new TweetControllerThread(documents, this);

        tweetControllerThread = new Thread(controllerClass);
        tweetControllerThread.start();
        tweetGrabber = new Thread(new TweetGrabber2(grabberCommand, documents));
        tweetGrabber.start();
    }
    public void setSession(Session session) {
        this.session = session;
    }

    /*
    stops everything
     */
    public void closeSession(String message){
        sendMessage(message, "endsearch");
        //tweetControllerThread.interrupt();
        tweetGrabber.interrupt();
    }



    public void closeSession() {
        closeSession("Tweet gathering has stopped");
    }

    public void closeAll() {
        tweetControllerThread.interrupt();
        tweetGrabber.interrupt();
    }

    public synchronized void sendCommand(JSONObject command, int tweetsToGather){
        System.out.println("Got Command");
        grabberCommand.add(command);
        controllerClass.setLimit(tweetsToGather);
    }
    /*
    send everything back to the web in reverse
     */
    public void sendBack() {
        for (int i=tweets.size()-1; i > 0; i--) {
            sendTweet(tweets.get(i), "tweet");
            //waiting(1);
        }
    }

    /*
    Method for sending a string back to the web
     */
    public boolean sendMessage(String message, String kind) {


        // sends the twitter message back to the web
        try{

            switch (kind){
                case "tweet":
                    session.getRemote().sendString("<u>"+message+"</u>");
                    break;
                case "endsearch":
                    String reply =
                            "<div class='hidden'>end</div>";
                    session.getRemote().sendString(reply);
                    break;
            }

            String json = "{/MSG/: /"+kind+"/, /VALUE/: /"+message+"/}";
            json = json.replace('/', '"');
            //session.getRemote().sendString(json);
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
    /*
    Start the profile crawler
     */
    public void profileCrawler(String profileName) {
        profileGrabber = new ProfileGrabber(profileName);
        sendProfile(profileGrabber);
    }

    public void sendProfile(ProfileGrabber profileGrabber){

        String profileHtml =
                    "<div class='tweet col-sm-12 col-md-12 col-lg-12 panel'>" +
                        //Profile Image
                            "<div class='col-sm-2 col-md-2 col-lg-2'>" +
                                "<img src='" + profileGrabber.getImageUrl().replace("400x400","bigger") + "' alt='Picture'>" +
                            "</div>" +
                            "<div class='col-sm-10 col-md-10 col-lg-10'>" +
                                "<div class='col-sm-12 col-md-12 col-lg-12'>" +
                                    "<b>" + profileGrabber.getName()+"</b> " + profileGrabber.getProfile() + "-" +
                                "</div>" +
                                "<div class='col-sm-12 col-md-6 col-lg-6'>" +
                                    "<div class='col-sm-12 col-md-12 col-lg-12'>" +
                                        "Joined on: " + profileGrabber.getJoinDate() + "<br>" +
                                        "Followers: " + profileGrabber.getFollowers() +"<br>" +
                                        "Following: " + profileGrabber.getFollowing()+ "<br>" +
                                        "Tweets: " + profileGrabber.getTweets()+ "<br>" +
                                        "Likes: " + profileGrabber.getLikes()+ "<br>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='col-sm-12 col-md-6 col-lg-6'>" +
                                    "<div class='col-sm-12 col-md-12 col-lg-12'>" +
                                        "Likes:    " + profileGrabber.getLikes() + "<br>" +
                                        "Location:" + profileGrabber.getLocation() +"<br>" +
                                        "Tweets:   " + profileGrabber.getFollowing()+ "<br>" +
                                    "</div>" +
                                "</div>" +
                            "</div>" +
                    "</div>";
        String script =
                "<script>" +
                    "$('#profile_area').append(\""+ profileHtml+"\")" +
                "</script>" ;

        try {
            session.getRemote().sendString(script);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendTweet(Tweet tweet, String kind) {
        // sends the twitter message back to the web
        try{

            String tweethtml =
                    "<div class='tweet col-md-12 col-sm-12 col-lg-12 panel '>" +
                        //Tweet Image
                        "<div class='col-md-2 col-sm-2 col-lg-2' id='tweetlink'>" +
                            "<img src='" + tweet.getPicture() + "' alt='Picture' onclick='searchProfile(\""+tweet.getUsername().substring(1)+"\")'>" +
                        "</div>" +
                        //Tweet Body
                        "<div class='col-md-10 col-sm-10 col-lg-10' >" +
                            "<div class='col-md-12 col-lg-12 col-sm-12' id='tweetlink' onclick='searchProfile(\""+tweet.getUsername().substring(1)+"\")'>" +
                                //Person / profile name
                                "<b>" + tweet.getProfilename()+"</b> " + tweet.getUsername() + " - <i>" +
                                //Time

                                    new Date((long)tweet.getTime()*1000) +

                            "</i></div>" +
                            "<div class='col-md-12 col-sm-12 col-lg-12'>" +
                                tweet.getMessage() +
                            "</div>" +
                            "<div class='col-md-12 col-sm-12 col-lg-12'>" +
                                "Retweets: " + tweet.getRetweets() +
                                "Likes: " + tweet.getLikes() +
                            "</div>" +
                        "</div>" +
                    "</div>";
            session.getRemote().sendString(tweethtml);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }
    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
    }
    public int getTweetsSize(){
        return tweets.size();
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
}
