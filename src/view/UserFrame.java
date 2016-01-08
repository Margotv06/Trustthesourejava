package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserFrame {

    public UserFrame() {
        JFrame frame = new JFrame("Trust The Source");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("This is Trust The Source Server, This needs to run on the background in order to get tweets good.");

        JButton button = new JButton();
        button.setText("Go to Trust The Source Terminal");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://www.trustthesource.nl/development/testing/terminal"));
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
