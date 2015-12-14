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
    private static final String LINK = "https://www.twitter.com/",
            SEARCH = "search?q=",
            // Here needs to be a search question
            SRC = "&src=typd";
    private Document document;
    private LinkedList<Document> documents;

    public TweetGrabber2(Stack<JSONObject> message, LinkedList<Document> documents) {
        this.message = message;
        this.documents = documents;
        System.out.println("TweetGrabber Initialized");
    }

    public void run() {
        while (true) {
            command = checkCommand();
            execute(command);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                    search = URLEncoder.encode(command.getString("VALUE"), "UTF-8");
                    System.out.println("TEST: search URL=" + search);
                }

                if (command.has("GET")) {
                    System.out.println("TEST: starting first fetch");
                    getFirstDoc();
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
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getFirstDoc() {
        try {
            System.out.println(LINK + SEARCH + search + SRC);
            document = Jsoup.connect(LINK + SEARCH + search + SRC).get();
            addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addDocument(Document document) {
        documents.push(document);
    }


}






class TweetContinueGrabber implements Runnable{
    private String search;
    private LinkedList<Document> documents;
    private  Document document;
    private String link;
    private int counted;
    private static final String BASEURL = "https://www.twitter.com/i/search/timeline?vertical=default&q=",
        // searchQuestion here
        SRC = "&src=typd",
        COMPOSED = "&composed_count=",
        INCLUDE_ENT = "&include_entities=1&include_new_items_bar=true&interval=30000",
        LATENT = "&latent_count=",
        MINPOS = "&max_position=";
    private String minpos;

    public TweetContinueGrabber(String search, Document document, LinkedList<Document> documents){
        counted = 0;
        this.search = search;
        synchronized (documents){this.documents = documents;}
        this.document = document;
        counted = document.select(".js-tweet-text.tweet-text").size();
        minpos = getMinPos(document);
        System.out.println("TweetContinueGrabber Inititalized");
        if(document!=null){
            System.out.println("Has Document");
        }
    }

    public void run() {
        while (true) {
            try {
                link = BASEURL + search + SRC;

                link += COMPOSED + getCountedComposed(document);
                link += INCLUDE_ENT;
                link += LATENT + getLatend(document);
                link += MINPOS + minpos;
                System.out.println(link);

                InputStream inputStream = new URL(link).openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(bufferedReader);
                JSONObject jsonObject = new JSONObject(jsonText);
                try {
                    minpos = jsonObject.get("max_position").toString();
                } catch (JSONException e) {
                    minpos = jsonObject.get("min_position").toString();
                }
                Object insideHtml = jsonObject.get("items_html");
                document = Jsoup.parse(insideHtml.toString());
                toStack(document);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
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
        if(document.getElementsByAttribute("data-min-position").attr("data-min-position")!=null){
            return document.getElementsByAttribute("data-min-position").attr("data-min-position");
        }
        return document.getElementsByAttribute("data-max-position").attr("data-max-position");
    }
    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }
    private synchronized void toStack(Document document){
        documents.push(document);
    }
}
