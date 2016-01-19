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
    private String name;
    private boolean verified;

    private int tweets;
    private int following;
    private int followers;
    private int likes;
    private String joinDate;
    private String location;
    private String imageUrl;
    private int amountOfPictures;

    public ProfileGrabber(String profile) {
        this.profile = profile;
        try {
            document = Jsoup.connect("http://twitter.com/"+profile).get();
        } catch(IOException e){}
        getProfileInfo();

    }
    private void getProfileInfo() {
        name = document.select("a.ProfileHeaderCard-nameLink").text();
        String verifiedString = document.select("span.ProfileHeaderCard-badges.ProfileHeaderCard-badges--1").text();
        if (verifiedString.contains("account")){
            verified = true;
        }
        else {
            verified = false;
        }

        tweets = Integer.parseInt(document.select("span.ProfileNav-value").get(0).text().replaceAll("\\D+",""));
        following = Integer.parseInt(document.select("span.ProfileNav-value").get(1).text().replaceAll("\\D+",""));
        followers = Integer.parseInt(document.select("span.ProfileNav-value").get(2).text().replaceAll("\\D+",""));
        likes = Integer.parseInt(document.select("span.ProfileNav-value").get(3).text().replaceAll("\\D+",""));

        joinDate = document.select("span.ProfileHeaderCard-joinDateText").first().attr("title");

        location = document.select("span.ProfileHeaderCard-locationText").text();
        imageUrl = document.select("img.ProfileAvatar-image").attr("src");
        String amountOfPicturesString = document.select("a.PhotoRail-headingWithCount").text().replaceAll("\\D+","");
        if (amountOfPicturesString.equals("")) {
            amountOfPictures = 0;
        }
        else {
            amountOfPictures = Integer.parseInt(amountOfPicturesString);
        }
        System.out.println(verified);
    }

    public static void main(String[] args) {
        new ProfileGrabber("barackobama");
    }


    public String getProfile() {
        return profile;
    }

    public Document getDocument() {
        return document;
    }

    public int getTweets() {
        return tweets;
    }

    public int getFollowing() {
        return following;
    }

    public int getFollowers() {
        return followers;
    }

    public int getLikes() {
        return likes;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
