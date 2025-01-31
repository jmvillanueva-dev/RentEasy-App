import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public JPanel loginPanel;
    private JTextField textField1;
    private JButton continuarButton;
    private JButton regresarButton;
    private JPasswordField passwordField2;

    public Login() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);

                if (currentFrame != null) {
                    // Cierra la ventana actual
                    currentFrame.dispose();
                }

                JFrame frame = new JFrame("Bienvenido");
                frame.setContentPane(new Welcome().welcomePanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,650);
                frame.setPreferredSize(new Dimension(700,650));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
