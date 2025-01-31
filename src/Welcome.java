import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome {
    private JButton iniciarSesiónButton;
    public JPanel welcomePanel;
    private JButton registrarseButton;

    public Welcome() {
        iniciarSesiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(welcomePanel);

                if (currentFrame != null) {
                    currentFrame.dispose();
                }

                JFrame frame = new JFrame("Login");
                frame.setContentPane(new Login().loginPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,650);
                frame.setPreferredSize(new Dimension(700,650));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(welcomePanel);

                if (currentFrame != null) {
                    currentFrame.dispose();
                }

                JFrame frame = new JFrame("Login");
                frame.setContentPane(new Register().registerPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,800);
                frame.setPreferredSize(new Dimension(700,800));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
