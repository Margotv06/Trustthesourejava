package controller;

import model.Tweet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.UUID;

import model.Tweet;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by pjvan on 30-11-2015.
 */
@WebSocket
public class WebClientConn {
    private TweetController tweetController = new TweetController();
    /**
     * Handling a closed connection
     * @param session session of request
     * @param statusCode status code
     * @param reason closing reason
     */
    @OnWebSocketClose
    public void onClose(Session session,int statusCode, String reason){
        System.out.println("Session closed id:"+session.getLocalAddress());


    }

    @OnWebSocketConnect
    public void onConnect(Session session){
        tweetController.setSession(session);
    }

    /**
     * Handling the command given by Session
     * @param session session with request
     * @param command command of request
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String command){
        try {
            JSONObject jsonObject = new JSONObject(command);
            String commands = jsonObject.get("COMMAND").toString();
            System.out.println(commands);
            switch(commands){
                case "get":
                    session.getRemote().sendString("Got Get Command");
                    tweetController.sendCommand(jsonObject);
                    break;
                case "":
                    session.getRemote().sendString("No Command");
                    break;
                default:
                    session.getRemote().sendString("Invallid Command");
                    break;
            }

        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketError
    public void onError(Throwable t){

    }


}