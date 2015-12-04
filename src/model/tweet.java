package model;

import controller.TweetGrabber;
import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.Date;
import java.text.* ;
import java.util.Locale;

/**
 * Created by  on 30-11-2015.
 */
public class Tweet
{
    private String message;
    private int retweets;
    private int likes;
    private Date time;
    private String profileName;
    private String userName;

    private String url;
    private ArrayList<String> hashtag;
    private ArrayList<String> links;

    private Session requestedSession;
    private Document doc;

    public Tweet(String tweet, Document doc){
        this.doc = doc;
        this.message = tweet;
        hashtag = searchHashtag(message);
        links = searchLinks(message);
        this.retweets = retweets();
        this.likes = likes();
        this.userName = userName();
        this.profileName = profileName();
        this.time = time();
    }

    /*
    Search for the amount of retweets a tweet has
     */
    private int retweets(){
        String retweetsString = TweetGrabber.getInfo("retweets", doc).text();
        retweetsString = retweetsString.replaceAll("\\D+","");
        return Integer.parseInt(retweetsString);
    }

    /*
    Search for the timestamp of the tweet
     */
    private Date time(){
        String timeString = TweetGrabber.getInfo("time", doc).text();
        String[] arrayTimeString = timeString.split(" ");
        String dateString = month(arrayTimeString[3]);
        dateString += " ";
        dateString += arrayTimeString[2];
        dateString += ", "+arrayTimeString[4];
        dateString += ", "+arrayTimeString[0];

        DateFormat format = new SimpleDateFormat("MMMM d, yyyy, h:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        }
        catch (ParseException e) {
        }
        return date;
    }

    /*
    Search for the amount of likes a tweet has
     */
    private int likes(){
        String likesString = TweetGrabber.getInfo("likes", doc).text();
        likesString = likesString.replaceAll("\\D+","");
        return Integer.parseInt(likesString);

    }

    /*
    Search for the username of the tweet
     */
    private String userName(){
        return TweetGrabber.getInfo("username", doc).text();
    }
    /*
    Search for the profilename of the tweet
     */
    private String profileName(){
        return TweetGrabber.getInfo("profilename", doc).text();
    }

    /*
    Search for hashtags in a twitter message
     */
    private ArrayList<String> searchHashtag(String message){
        String[] words = message.split(" ");
        ArrayList<String> hashtags = new ArrayList<String>();
        for (String word : words){
            if(word.startsWith("#")){
                hashtags.add(word);
            }
        }
        return hashtags;
    }

    /*
    Search for links in a twitter message
     */
    private ArrayList<String> searchLinks(String message){
        String[] words = message.split(" ");
        ArrayList<String> links = new ArrayList<String>();
        for (String word : words){
            if(word.startsWith("http")){
                links.add(word);
            }
        }
        return links;
    }
    private String month(String month) {
        switch(month) {
            case "jan.":
                return "January";
            case "feb.":
                return "February";
            case "mar.":
                return "March";
            case "apr.":
                return "April";
            case "may.":
                return "May";
            case "jun.":
                return "June";
            case "jul.":
                return "July";
            case "aug.":
                return "August";
            case "sep.":
                return "September";
            case "oct.":
                return "October";
            case "nov.":
                return "November";
            case "dec.":
                return "December";
        }
        return "";
    }


    public String getMessage(){
        return message;
    }
    public Date getTime(){
        return time;
    }
    public int getRetweets() {
        return retweets;
    }
    public int getLikes() {
        return likes;
    }
    public String getUsername() {
        return userName;
    }
    public String getProfilename() {
        return profileName;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<String> getHashtag() {
        return hashtag;
    }
}
