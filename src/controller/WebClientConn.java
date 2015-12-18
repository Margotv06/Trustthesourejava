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
        tweetController.closeSession();

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
                    String json = "{/MSG/: / info /, /VALUE/: /Got get command./}";
                    json = json.replace('/', '"');
                    session.getRemote().sendString(json);
                    tweetController.sendCommand(jsonObject);

                    break;
                case "":
                    String json2 = "{/MSG/: / info /, /VALUE/: /No command given./}";
                    json = json2.replace('/', '"');
                    session.getRemote().sendString(json2);

                    break;
                case "send":
                    String json3 = "{/MSG/: / info /, /VALUE/: /Got send command/}";
                    json = json3.replace('/', '"');
                    session.getRemote().sendString(json3);

                    String word = jsonObject.get("DELETE").toString();
                    String[] words = word.split(" ");
                    tweetController.updateTweetList(words);
                    break;
                case "stop":
                    String json4 = "{/MSG/: / info /, /VALUE/: /Stop command issued/}";
                    json = json4.replace('/', '"');
                    session.getRemote().sendString(json4);
                    tweetController.closeSession();
                    break;
                default:
                    String json5 = "{/MSG/: / info /, /VALUE/: /Invalid command/}";
                    json = json5.replace('/', '"');
                    session.getRemote().sendString(json5);
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