package model;

import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pjvan on 30-11-2015.
 */
public class Tweet
{
    private String message;
    private int retweets;
    private String url;
    private ArrayList<String> hashtag;
    private ArrayList<String> links;
    private Session requestedSession;

    public void Tweet(String urls, Session requestedSessions){
        url = urls;
        requestedSession = requestedSessions;
        Document doc = null;
        try{
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.err.println(e);
            }
            message = doc.select("p.js-tweet-text.tweet-text").first().text();
            //retweets= Integer.parseInt(doc.select("tweetid").first().text());
            hashtag=searchHashtag(message);
            links = searchLinks(message);

        }
        catch (NullPointerException nullp){
            System.err.println(nullp);
        }
    }

    private ArrayList<String> searchHashtag(String message){
        String[] words = message.split(" ");
        ArrayList<String> hashtags = new ArrayList<String>();
        for (String word : words){
            if(word.startsWith("#")){
                hashtags.add(word);
                try {
                    requestedSession.getRemote().sendString("Founded HashTag:" + word);
                }catch (IOException e){
                    System.err.println(e);
                }
            }
        }
        return hashtags;
    }

    private ArrayList<String> searchLinks(String message){
        String[] words = message.split(" ");
        ArrayList<String> links = new ArrayList<String>();
        for (String word : words){
            if(word.startsWith("http")){
                links.add(word);
                try {
                    requestedSession.getRemote().sendString("Founded Link: <a href='" + word+ "'> "+word+"</a>");
                }catch (IOException e){
                    System.err.println(e);
                }
            }
        }
        return links;
    }


    public String getMessage(){
        return message;
    }
    public int getRetweets() {
        return retweets;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<String> getHashtag() {
        return hashtag;
    }
}
