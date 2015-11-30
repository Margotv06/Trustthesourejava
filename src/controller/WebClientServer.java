package controller;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by pjvan on 30-11-2015.
 */
public class WebClientServer{

    /**
     * Creating new server and Initializing a session listener.
     * @throws Exception
     */
    public static void main() throws Exception {
        Server wsServer = new Server(31515);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(WebClientConn.class);
            }

        };
        wsServer.setHandler(wsHandler);
        wsServer.start();
        wsServer.join();
    }
}
