package controller;

import org.jsoup.Jsoup;
import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by daant on 15-Jan-16.
 */
public class ProfileGrabber {
    private Document document;
    private String profile;

    private int tweets;
    private int following;
    private int followers;
    private int likes;
    private String joinDate;
    private String location;
    private String imageUrl;

    public ProfileGrabber(String profile) {
        this.profile = profile;
        try {
            document = Jsoup.connect("http://twitter.com/"+profile).get();
        } catch(IOException e){}
        getProfileInfo();

    }
    private void getProfileInfo() {
        tweets = Integer.parseInt(document.select("span.ProfileNav-value").get(0).text().replaceAll("\\D+",""));
        following = Integer.parseInt(document.select("span.ProfileNav-value").get(1).text().replaceAll("\\D+",""));
        followers = Integer.parseInt(document.select("span.ProfileNav-value").get(2).text().replaceAll("\\D+",""));
        likes = Integer.parseInt(document.select("span.ProfileNav-value").get(3).text().replaceAll("\\D+",""));
        joinDate = document.select("span.ProfileHeaderCard-joinDateText").text();
        location = document.select("span.ProfileHeaderCard-locationText").text();
        imageUrl = document.select("img.ProfileAvatar-image").attr("src");

        System.out.println(tweets);
        System.out.println(following);
        System.out.println(followers);
        System.out.println(likes);
        System.out.println(joinDate);
        System.out.println(location);
        System.out.println(imageUrl);
    }
    public static void main(String [ ] args) {
        ProfileGrabber profileGrabber = new ProfileGrabber("Bol_com_games");
    }
}
