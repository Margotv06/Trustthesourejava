package model;

import controller.TweetGrabber;
import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.text.* ;
import java.util.Locale;

/**
 * Created by  on 30-11-2015.
 */
public class Tweet
{
    private int position;
    private String message;
    private ArrayList<String> messageWords;
    private int retweets;
    private int likes;
    private String time;
    private String profileName;
    private String userName;
    private String picture;

    private String url;
    private ArrayList<String> hashtag;
    private ArrayList<String> links;

    private Session requestedSession;
    private Document doc;

    public Tweet(String tweet, Document doc, int pos){
        this.position = pos;
        this.doc = doc;
        this.message = tweet;
        hashtag = searchHashtag(message);
        links = searchLinks(message);
        this.retweets = retweets();
        this.likes = likes();
        this.userName = userName();
        this.profileName = profileName();
        this.time = timeUnix();
        this.messageWords = getKeyWords(message);
        this.picture = picture();
    }

    /*
    Searches for the keywords in the tweet and returns them
     */
    private ArrayList<String> getKeyWords(String message) {
        message = message.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        ArrayList<String> messageWords = new ArrayList<String>(50);
        String[] messageArray = message.split(" ");

        for (String word: messageArray) {
            if (checkWord(word)) {
                messageWords.add(word);
            }
        }
        return messageWords;
    }
    /*
    checks if a string is in the ignore list
    returns false if word is  in ignoreList
     */
    private boolean checkWord(String word) {
        String[] ignoreList = IgnoreList.getIgnoreList();

        for (String ignoreWord: ignoreList) {
            if (ignoreWord.equals(word)) {
                return false;
            }
            if (word.contains("http")) {
                return false;
            }
            if (word.contains("pic")) {
                return false;
            }
        }
        return true;
    }

    /*
    Search for the amount of retweets a tweet has
     */
    private int retweets(){

        Element element = IgnoreList.getInfo("retweets", doc, position);

        if (element == null) {
            return 0;
        }

        String retweetsString = element.text();
        retweetsString = retweetsString.replaceAll("\\D+","");
        return Integer.parseInt(retweetsString);
    }

    /*
    Search for the timestamp of the tweet
     */
    private Date time(){
        String timeString = IgnoreList.getInfo("time", doc, position).text();
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
   Search for the timestamp of the tweet
    */
    private String timeUnix(){
        return IgnoreList.getInfo("time", doc, position).attr("data-time");

    }

    /*
    Search for the amount of likes a tweet has
     */
    private int likes(){
        Element element = IgnoreList.getInfo("likes", doc, position);

        if (element == null) {
            return 0;
        }

        String likesString = element.text();
        likesString = likesString.replaceAll("\\D+","");
        return Integer.parseInt(likesString);

    }

    /*
    Search for the username of the tweet
     */
    private String userName(){
        return IgnoreList.getInfo("username", doc, position).text();
    }
    /*
    Search for the profilename of the tweet
     */
    private String profileName(){
        return IgnoreList.getInfo("profilename", doc, position).text();
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
    /*
    Search for the username of the tweet
     */
    private String picture() {
        return IgnoreList.getInfo("picture", doc, position).attr("src");
    }

    public String getPicture() {

        return picture;
    }
    public String getMessage(){
        return message;
    }
    public String getTime(){
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

    public ArrayList<String> getMessageWords() { return messageWords;}
}
