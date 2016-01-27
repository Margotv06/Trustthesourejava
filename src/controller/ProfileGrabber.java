package controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

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

    /*
    Method for getting all the profile info
     */
    private void getProfileInfo() {
        Element nameElement = document.select("a.ProfileHeaderCard-nameLink").first();
        if (nameElement != null) {
            name = document.select("a.ProfileHeaderCard-nameLink").text();
        }
        else { name = "Name is not available"; }

        String verifiedString = document.select("span.ProfileHeaderCard-badges.ProfileHeaderCard-badges--1").text();
        if (verifiedString.contains("account")){
            verified = true;
        }
        else {
            verified = false;
        }
        // Lets set some default values
        followers = 0;
        likes = 0;
        tweets = 0;
        following = 0;

        // Getting the elements for getting the amount of followers, likes, tweets and following
        Elements barValue = document.select("span.ProfileNav-value");
        Elements barLabel = document.select("span.ProfileNav-label");

        System.out.println("size: "+barValue.size());

        for (int i = 0; i != barValue.size(); i++ ) {
            String something = barValue.get(i).text(); // still needs to be determined what it is
            String somethingLabel = barLabel.get(i).text();

            System.out.println("tweets:" +somethingLabel);
            if (somethingLabel.contains("Tweets")) {

                tweets = Integer.parseInt(something.replaceAll("\\D+",""));
            }
            if (somethingLabel.contains("Volgend")) {
                following = Integer.parseInt(something.replaceAll("\\D+",""));
            }
            if (somethingLabel.contains("Volgers")) {
                followers = Integer.parseInt(something.replaceAll("\\D+",""));
            }
            if (somethingLabel.contains("Vind-ik-leuks")) {
                likes = Integer.parseInt(something.replaceAll("\\D+",""));
            }

        }

        // Sets the joinDate
        Element joinDateElement = document.select("span.ProfileHeaderCard-joinDateText").first();
        if (joinDateElement != null) {
            joinDate = joinDateElement.attr("title");
        }
        else {
            joinDate = "Onbekend";
        }
        // Sets the location
        location = document.select("span.ProfileHeaderCard-locationText").text();
        if (!document.select("span.ProfileHeaderCard-locationText").hasText()) {
            location = "Onbekend";
        }

        // Sets the profile picture url
        imageUrl = document.select("img.ProfileAvatar-image").attr("src");
        String amountOfPicturesString = document.select("a.PhotoRail-headingWithCount").text().replaceAll("\\D+","");
        if (amountOfPicturesString.equals("")) {
            amountOfPictures = 0;
        }
        else {
            amountOfPictures = Integer.parseInt(amountOfPicturesString);
        }
    }

    /*
    Getters
     */
    public String getProfile() {
        return profile;
    }
    public boolean getVerified() { return verified;}
    public int getAmountOfPictures() { return amountOfPictures;}
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
    public String getName() {
        return name;
    }
}
