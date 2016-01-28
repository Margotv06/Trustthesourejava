package controller;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 *    ______                __  __  __        _____
 *   /_  __/______  _______/ /_/ /_/ /_  ___ / ___/____  __  _______________
 *    / / / ___/ / / / ___/ __/ __/ __ \/ _ \\__ \/ __ \/ / / / ___/ ___/ _ \
 *   / / / /  / /_/ (__  ) /_/ /_/ / / /  __/__/ / /_/ / /_/ / /  / /__/  __/
 *  /_/ /_/   \__,_/____/\__/\__/_/ /_/\___/____/\____/\__,_/_/   \___/\___/
 *
 *  The TrusttheSource WebClientServer,
 *  This is the class that handles connections between webclients and the server. \
 *
 *  Clients connect to Port 31515
 *  When a client connect to the server, a class called, WebCliendConn will be initialized.
 *  The WebClientConn will continue the process.
 */
public class WebClientServer{

    /**
     * Creating new server and Initializing a session listener.
     * @throws Exception
     */
    public static void main() throws Exception {
        //Create a Server on Port 31515. This Port is also used on the TrusttheSource website
        //Changing this port, will need editing on the website under:
        //{TrusttheSourceRoot}/public/js/terminal.js
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
