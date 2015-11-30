import controller.WebClientServer;

import java.io.IOException;

/**
 * Created by pjvan on 30-11-2015.
 */
public class Main {
    public static WebClientServer webServer;

    public static void main(String[] args) {
        System.out.println("Starting the Main.class");
        try{
            WebClientServer.main();
        }
        catch (Exception e){
            System.err.println(e);
        }

    }
}
