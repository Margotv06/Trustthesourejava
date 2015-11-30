package controller;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

/**
 * Created by pjvan on 30-11-2015.
 */
@WebSocket
public class WebClientConn {

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

    }

    /**
     * Handling the command given by Session
     * @param session session with request
     * @param command command of request
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String command){
        String[] args = command.split(" ");
        String line="";
        switch (args[0]) {
            case "GET":

                for(String a: args){
                    line+=a+ "|";
                }
                System.out.println(line);
                TweetGrabber.TweetGrabber(args[1]);
                break;
            case "GET_COUNTRY":
                for(String a: args){
                    line+=a+ "|";
                }
                args[1].replaceAll("_"," ");
                System.out.println(line);

                break;

            default:
                try {
                    session.getRemote().sendString("invalid command.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @OnWebSocketError
    public void onError(Throwable t){

    }

}