import controller.WebClientServer;
import view.UserFrame;

import javax.swing.*;
import java.io.IOException;

/**
 *    ______                __  __  __        _____
 *   /_  __/______  _______/ /_/ /_/ /_  ___ / ___/____  __  _______________
 *    / / / ___/ / / / ___/ __/ __/ __ \/ _ \\__ \/ __ \/ / / / ___/ ___/ _ \
 *   / / / /  / /_/ (__  ) /_/ /_/ / / /  __/__/ / /_/ / /_/ / /  / /__/  __/
 *  /_/ /_/   \__,_/____/\__/\__/_/ /_/\___/____/\____/\__,_/_/   \___/\___/
 *
 * This is the main class of TrusttheSource
 *
 * within this class there will be an UserFrame created and a webClientServer.
 *
 * The Function of the UserFrame is to have a noticeable UserInterface to show that the program is running.
 * On closing this UserFrame, the program will stop.
 *
 * The function of the WebClientServer is to handle connections and fetch/crawl data from Twitter.
 * More info on the WebClientServer pages it self..
 */
public class Main {
    public static WebClientServer webServer;

    public static void main(String[] args) {
        System.out.println("Starting the Main.class");
        new UserFrame();
        try{
            WebClientServer.main();

        }

        catch (Exception e){
            System.err.println(e);
        }

    }
}
