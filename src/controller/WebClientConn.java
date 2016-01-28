package controller;

import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import model.Tweet;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *    ______                __  __  __        _____
 *   /_  __/______  _______/ /_/ /_/ /_  ___ / ___/____  __  _______________
 *    / / / ___/ / / / ___/ __/ __/ __ \/ _ \\__ \/ __ \/ / / / ___/ ___/ _ \
 *   / / / /  / /_/ (__  ) /_/ /_/ / / /  __/__/ / /_/ / /_/ / /  / /__/  __/
 *  /_/ /_/   \__,_/____/\__/\__/_/ /_/\___/____/\____/\__,_/_/   \___/\___/
 *
 *  This is the TrusttheSource WebClientConn, This is the main method that handles requests from the session that was
 *  initialized.
 *
 *  There is a ActionListener that will listen to request comments. Command received from the session needs to be in
 *  JSON format to handle.
 *
 *
 */
@WebSocket
public class WebClientConn {

    private TweetController tweetController = new TweetController();
    private Session session;
    /**
     * Handling a closed connection
     * @param session session of request
     * @param statusCode status code
     * @param reason closing reason
     */
    @OnWebSocketClose
    public void onClose(Session session,int statusCode, String reason){
        System.out.println("Session closed id:"+session.getLocalAddress());
        tweetController.closeSession();

    }

    /**
     * On connect the tweetcontroller will set the session with the session thats connected.
     * @param session
     */
    @OnWebSocketConnect
    public void onConnect(Session session){
        tweetController.setSession(session);
        this.session = session;
    }

    /**
     * Handling the command given by Session.
     * Commands needs to be in JSON Format.
     * @param session session with request
     * @param command command of request
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String command){
        try {
            //Send command to terminal as control. This is for testing purposes.
            System.out.println(command);
            //Parse the command that is retrieved as a String to a JSONObject.
            JSONObject jsonObject = new JSONObject(command);
            //Get the value of COMMAND, A command will determine what will happen next on.
            String commands = jsonObject.get("COMMAND").toString();
            //Send the COMMAND value to the terminal. This is for testing purposes.
            System.out.println(commands);
            //Handle the COMMAND by a switch statement.
            switch(commands){
                /**
                 * In case of get command, the social media crawler will be activated.
                 */
                case "get":
                    handleGet(jsonObject);
                    break;
                /**
                 * Stop command to halt the furthering crawling of social media. And return a request to invert search.
                 */
                case "stop":
                    tweetController.closeSession();
                    break;
                /**
                 * Command to invert search, this will re-send the oldest tweet at first.
                 * And send a new Button to the Terminal to re-revert the data. (to enable start command)
                 */
                case "end":
                    handleEnd();
                    break;
                /**
                 * Command to re-invert search to the default setting. Newest first.
                 * And send a button to revert seach on terminal view (to enable end command)
                 */
                case "start":
                    handleStart();
                    break;
                /**
                 * Command to get a profiles detail.
                 */
                case "profile":
                    String profileName = jsonObject.get("NAME").toString();
                    tweetController.profileCrawler(profileName);
                    break;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketError
    public void onError(Throwable t){
        tweetController.closeSession();
    }


    private void handleGet(JSONObject jsonObject) throws IOException, JSONException {
        if (this.session == session) {
            //tweetController.closeAll();
            this.tweetController = new TweetController();
            tweetController.setSession(session);
        }

        //Get the minimum limit of tweets needed from the get command.
        //When the amount of tweets extends the limit, the crawler should stop searching.
        // 0 stands for infinite.
        String tweetsToGatherString = jsonObject.get("LIMIT").toString();
        int tweetsToGather = Integer.parseInt(tweetsToGatherString);

        //Return a reply to the WebApplication that the command is received successfully.
        //This Message will disappear after 2.8 seconds. This is the estemated ammount of time,
        //the program needs to get the first tweets as a reply to the website.
        Random rand = new Random();
        int randomint = rand.nextInt(50)+1;
        String reply = "<div class='h4' id='pleaseWait"+randomint+"'>Commando ontvangen, even geduld A.U.B.<img src='/image/preloader.gif' alt='loading' style='height: 3%'></div>" +
                "<script>" +
                "   setTimeout(function() {\n" +
                "           $('#pleaseWait"+randomint+"').fadeOut('fast');\n" +
                "       }, 2800); " +
                "</script>";
        session.getRemote().sendString(reply);

        //Send the remaining command to TweetController. for further processing.
        tweetController.sendCommand(jsonObject, tweetsToGather);

    }
    private void handleEnd() throws IOException {
        String rereverse = "<div class='hidden'>re-end</div>";
        session.getRemote().sendString(rereverse);
        tweetController.sendBack();

    }
    private void handleStart() throws IOException {
        String reverse = "<div class='hidden'>end</div>";
        session.getRemote().sendString(reverse);
        tweetController.sendStart();
    }
    private void handleDefault() {

    }


}