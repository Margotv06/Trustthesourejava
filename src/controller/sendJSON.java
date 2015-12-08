package controller;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONObject;

/**
 * Created by pjvan on 4-12-2015.
 */
public class SendJSON {
    private String link;

    public static void main(String[] args){
        new SendJSON().SendJSON();
    }

    public void SendJSON(){
        System.out.println("TESTING SEND JSON");
        try{
            Document document = Jsoup.connect("https://www.twitter.com/search?q=paris&src=typd").get();
            String question = "paris";
            System.out.println("Start generating Link");
            SendJSON(question, document);
            while (true) {
                System.out.println("\nFetching new JSON");
                InputStream inputStream = new URL(link).openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(bufferedReader);
                JSONObject jsonobject =  new JSONObject(jsonText);
                String dataCorrected;
                try{
                    dataCorrected = jsonobject.get("max_position").toString();
                }catch (JSONException e){
                    dataCorrected = jsonobject.getString("min_position");
                }
                Object htlm = jsonobject.get("items_html");
                Document doc = Jsoup.parse(htlm.toString());

                try{

                    for(int i =0 ;  i< doc.select(".js-tweet-text.tweet-text").size();i++) {
                        System.out.println(doc.select(".js-tweet-text.tweet-text").get(i).text());

                    }
                }catch (NullPointerException e ){e.printStackTrace();}

                link=null;
                link = "https://www.twitter.com/i/search/timeline?vertical=default";
                link += getQuestion(link, question);
                link += "&src=typd";
                link += "&composed_count="+getTweetCounted(document);
                link += "&include_entities=1";
                link += "&include_new_items_bar=true";
                link += "&interval=30000";
                link += "&latent_count="+getLatendCount()+11;
                link += "&max_position="+dataCorrected;

            }

        }catch (IOException | JSONException e){
            System.err.println(e);
        }

    }

    public void SendJSON(String question, Document document){
        link = "https://www.twitter.com/i/search/timeline?vertical=default";
        link += getQuestion(link, question);
        link += "&src=typd";
        link += "&composed_count="+getTweetCounted(document);
        link += "&include_entities=1";
        link += "&include_new_items_bar=true";
        link += "&interval=30000";
        link += "&latent_count="+getLatendCount()+11;
        link += "&min_position="+getMinPosition(document);
        System.out.println(link);

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readJsonFromUrl(String url){
        try {
            InputStream is = new URL(url).openStream();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                return readAll(rd);
            } finally {
                is.close();
            }
        }catch (IOException e){
            System.err.println(e);
        }
        return null;
    }







    private String getQuestion(String link, String question){
        return "&q="+htmlString(question);
    }

    private String getTweetCounted(Document document){
        return ""+document.select("p.js-tweet-text.tweet-text").size();
    }

    private String getLatendCount(){
        return ""+0;
    }

    private String getMinPosition(Document document){
        String s = document.getElementsByAttribute("data-min-position").attr("data-min-position");
        //String value =  "TWEET";
        //value += "-SESSIONUNKNOWN";   // This value is Updated on Scroll down at Min
        String value = "-"+s;           // This Value is Updated overtime       at Max
        //value += "-HASHUNKNOWN";      // This Hash  is
        return value;
    }

    private String htmlString(String string){

        return string;
    }
}
