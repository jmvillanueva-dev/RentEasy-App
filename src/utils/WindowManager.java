package utils;

import javax.swing.*;
import java.awt.*;

public class WindowManager {

    public static void navigateTo(JFrame currentFrame, JPanel newPanel, String title) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        JFrame frame = new JFrame(title);
        frame.setContentPane(newPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1180, 800);
        frame.setPreferredSize(new Dimension(1180, 800));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //frame.setResizable(false);
    }
}
