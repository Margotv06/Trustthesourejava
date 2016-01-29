package controller;

import model.Tweet;

/**
 * Created by daant on 29-Jan-16.
 */
public class KeepAlive implements Runnable  {
    private TweetController tweetController;

    KeepAlive(TweetController tweetController) {
        this.tweetController = tweetController;
    }

    @Override
    public void run() {
        while(true) {
            waiting(100000);
            System.out.println("kept alive");
            tweetController.sendMessage("keepalive", "keepalive");
        }
    }
    /*
waits
 */
    synchronized private void waiting(int ms) {
        try {
            this.wait(ms);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return;
    }
}
