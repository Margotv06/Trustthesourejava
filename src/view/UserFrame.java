package view;
/**
 *
 *    ______                __  __  __        _____
 *   /_  __/______  _______/ /_/ /_/ /_  ___ / ___/____  __  _______________
 *    / / / ___/ / / / ___/ __/ __/ __ \/ _ \\__ \/ __ \/ / / / ___/ ___/ _ \
 *   / / / /  / /_/ (__  ) /_/ /_/ / / /  __/__/ / /_/ / /_/ / /  / /__/  __/
 *  /_/ /_/   \__,_/____/\__/\__/_/ /_/\___/____/\____/\__,_/_/   \___/\___/
 *
 *  This class makes the UserInterface. The User Interface is still limited, and the main goal
 *  for this class is to show the user that the program is running plus, on closing the closure
 *  of the application it self.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Initializing UserFrame
 */
public class UserFrame {
    /**
     * The Main class of UserFrame. This is still a single method, that needs to split up,
     * ones it will get bigger.
     */
    public UserFrame() {
        //The title
        JFrame frame = new JFrame("TrusttheSource");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        //The Test
        JLabel label = new JLabel("This is TrusttheSource application that needs to run in combination of the website of TrusttheSource.");

        //Make a Button to go to the TrusttheSource Website.
        JButton button = new JButton();
        button.setText("Go to TrusttheSource Website");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //The ActionListener that will go to the Website.
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://trustthesource.nl/search_social_media"));
                }
                catch (java.io.IOException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        });

        panel.add(label);
        panel.add(button);

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
