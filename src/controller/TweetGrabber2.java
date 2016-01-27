package controller;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.json.JSONObject;



/**
 * Created by pjvan on 4-12-2015.
 */
public class TweetGrabber2 implements Runnable {
    private String link;
    //private static List<JSONObject> message= Collections.synchronizedList(new LinkedList<>());
    private Stack<JSONObject> message;
    private JSONObject command;
    private String search;
    private Thread grabber;
    private final String LINK = "https://www.twitter.com/",
            SEARCH = "search?",
            LIVETWEET = "&f=tweets",
            // Here needs to be a search question
            SRC = "&src=typd";

    private Document document;
    private LinkedList<JSONObject> documents;

    public TweetGrabber2(Stack<JSONObject> message, LinkedList<JSONObject> documents) {
        this.message = message;
        this.documents = documents;
        System.out.println("TweetGrabber Initialized");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            command = checkCommand();
            execute(command);
            waiting();
        }
        grabber.interrupt();
    }
    /*
    waits
     */
    synchronized private void waiting() {
        try {
            this.wait(1000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            grabber.interrupt();
        }
        return;
    }

    private synchronized JSONObject checkCommand() {
        if(message.size()!=0)
            return message.pop();
        return null;

    }

    private void execute(JSONObject command) {
        if(command!=null) {
            try {
                if (command != null)
                    if (command.length() != 0) {
                        command.names().toString();
                    }
                if (command.has("VALUE")) {
                    System.out.println("Search request received");
                    search = command.getString("VALUE");
                    System.out.println("TEST: search URL=" + search);
                }

                if (command.has("GET")) {
                    System.out.println("TEST: starting first fetch");
                    getFirstDoc(command.get("LIVETWEET"));
                    if (command.get("GET").equals("ALL")) {
                        grabber = new Thread(new TweetContinueGrabber(search, document, documents));
                        grabber.start();
                    }
                }
                if (command.has("STOP")) {
                    grabber.interrupt();
                }
                if (command.has("CONTINUE")) {
                    grabber.notify();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getFirstDoc(Object livetweet) {
        try {
            System.out.println("LIVETWEET:"+livetweet);
            if(livetweet.equals("TRUE")){
                System.out.println(LINK + SEARCH + LIVETWEET + "&q="+ search + SRC);
                document = Jsoup.connect(LINK + SEARCH + LIVETWEET + "&q=" + search + SRC).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get();
            }else{
                System.out.println(LINK + SEARCH + search + SRC);
                document = Jsoup.connect(LINK + SEARCH + "q="+search + SRC).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get();
            }
            JSONObject jdocument = new JSONObject();
            jdocument.put("items_html", document.toString());
            addDocument(jdocument);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addDocument(JSONObject document) {
        documents.push(document);
    }


}






class TweetContinueGrabber implements Runnable{
    private String search;
    private LinkedList<JSONObject> documents;
    private  Document document;
    private String link;
    private Boolean doubled;
    private int counted;
    private static final String BASEURL = "https://www.twitter.com/i/search/timeline?vertical=default",
        // searchQuestion here
        LIVETWEET = "&f=tweets",
        SRC = "&src=typd",
        COMPOSED = "&composed_count=",
        INCLUDE_ENT = "&include_entities=1&include_new_items_bar=true&interval=30000",
        LATENT = "&latent_count=",
        MINPOS = "&max_position=";
    private String minpos,
        newest_tweet,
        oldest_tweet;


    public TweetContinueGrabber(String search, Document document, LinkedList<JSONObject> documents){
        counted = 0;
        this.search = search;
        synchronized (documents){this.documents = documents;}
        this.document = document;
        counted = document.select(".js-tweet-text.tweet-text").size();
        try{
            System.out.println(documents.peek().keys().toString());
            minpos = getMinPos(Jsoup.parse(
                    documents.peek().get("items_html").toString()));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        System.out.println("TweetContinueGrabber Inititalized");
        if(document!=null){
            System.out.println("Has Document");
            System.out.println(document.toString());
        }
        newest_tweet = document.getElementsByClass("tweet").attr("data-item-id").toString();
        System.out.println("newest ID: "+newest_tweet);
        for(int i=0;i<document.getElementsByClass("tweet").size();i++){
            if((document.getElementsByClass("tweet").get(i).attr("data-item-id").toString())!=null&&document.getElementsByClass("tweet").get(i).attr("data-item-id").toString()!=""){
                oldest_tweet = document.getElementsByClass("tweet").get(i).attr("data-item-id").toString();
            }
        }

        System.out.println("oldest ID: "+oldest_tweet);
    }

    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                link = BASEURL;
                if(1==1){
                    link += LIVETWEET;
                }
                link += "&q=" + search + SRC;

                link += COMPOSED + getCountedComposed(document);
                link += INCLUDE_ENT;
                link += LATENT + getLatend(document);
                //link += "&max_position=TWEET-"+oldest_tweet+"-"+newest_tweet;
                link += minpos;
                System.out.println(link);

                InputStream inputStream = new URL(link).openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(bufferedReader);
                JSONObject jsonObject = new JSONObject(jsonText);
                System.out.println(jsonText);
                try {
                    minpos = "&max_position="+ jsonObject.get("max_position").toString();
                } catch (JSONException e) {

                    minpos = "&min_position="+jsonObject.get("min_position").toString();
                }
                Object insideHtml = jsonObject.get("items_html");
                document = Jsoup.parse(insideHtml.toString());
                for(int i=0;i<document.getElementsByClass("tweet").size();i++){
                    if((document.getElementsByClass("tweet").get(i).attr("data-item-id").toString())!=null&&document.getElementsByClass("tweet").get(i).attr("data-item-id").toString()!=""){
                        oldest_tweet = document.getElementsByClass("tweet").get(i).attr("data-item-id").toString();
                    }
                }
                toStack(jsonObject);

                if(jsonObject.get("has_more_items").toString()=="false"){
                    System.out.println("has no new items");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private int getCountedComposed(Document document){
        counted += document.select(".js-tweet-text.tweet-text").size();
        return counted;
    }
    private int getLatend(Document document){
        return 0;
    }

    private String getMinPos(Document document){
        String position = null;
        if(document.getElementsByAttribute("data-min-position").attr("data-min-position")!=null){
            position = "&min_position="+document.getElementsByAttribute("data-min-position").attr("data-min-position").toString();
        }else {
            position = "&max_position="+document.getElementsByAttribute("data-max-position").attr("data-max-position").toString();
        }
        if(!position.equals("TWEET--")){
            return position;
        }else {
            //System.out.println(document.toString());
            System.err.println("Empty Pos fix, Old:"+position);
            position = null;
            position = document.getElementsByClass("stream-container").attr("data-max-position").toString();
            System.err.println("new:"+position);
            return position;
        }


    }
    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }
    private synchronized void toStack(JSONObject document){
        documents.push(document);
    }
}
