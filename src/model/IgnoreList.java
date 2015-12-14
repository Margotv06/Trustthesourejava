package model;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by daant on 09-Dec-15.
 */
public class IgnoreList {
    public static String[] getIgnoreList() {
        String[] ignoreList = {"ik", "mijn", "je", "jouw", "u", "uw", "hij", "haar", "het",
                "zijn", "we", "ons", "onze", "jullie", "ze", "hun", "me", "mezelf", "jezelf",
                "uzelf", "zich", "wie", "welk", "wat", "deze", "dat", "dit", "die", "hoe",
                "hoezo", "waarom", "met", "zonder", "wel", "en", "door, voor", "naast", "achter",
                "links", "rechts", "nu", "via", "ben", "jij", "nou", "een", "in", "uit", "wij",
                "te", "is", "ten", "als", "door", "van", "den", "de", "aan", "heb", "waar",
                "dus", "ook", "ter", "op", "word", "wordt", "mee", "naar", "iets", "zoals", "was",
                "ooit", "ligt", "er", "bij", "nog", "net", "voor", "om", "af", "kan", "zo",
                "dan", "ene", "over", "ok", "hoewat", "maar", "tegen", "hebt"};
        return ignoreList;
    }
    /*
   Method for getting information from the DOM
    */
    public static Element getInfo(String key, Document doc, int pos){

        Elements element =  doc.select(keyTable(key)).eq(pos);
        return element.first();
    }
    private static String keyTable(String key){
        switch (key){
            // Returns the actual content of the tweet
            case "tweet":
                return "p.js-tweet-text.tweet-text";

            // Returns the profilename
            case "profilename":
                return "strong.js-action-profile-name";

            // Returns the username
            case "username":
                return "span.js-action-profile-name";

            // Returns the amount of retweets (in String)
            // example: "Retweets: (number of RT)"
            case "retweets":
                return "li.js-stat-retweets";

            // Returns the amount of likes/favorites (in String)
            // example: "Retweets: (number of RT)"
            case "likes":
            case "favorites":
                return "li.js-stat-favorites";

            // Returns the time the tweet was send
            case "time":
                return "span.metadata";
        }
        return key;
    }
}
