import database.UserDAO;
import utils.WindowManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public JPanel loginPanel;
    private JTextField userEmail;
    private JPasswordField userPassword;
    private JButton continuarButton;
    private JButton backPanelButton;


    public Login() {

        // Navegación a la ventana de Bienvenida
        backPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);
                WindowManager.navigateTo(currentFrame, new Welcome().welcomePanel, "RentEasy | Bienvenido");
            }
        });

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = userEmail.getText();
                String password = new String(userPassword.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos obligatorios ( * ).");
                    return;
                }

                UserDAO userDAO = new UserDAO();
                if (userDAO.validateCredentials(email, password)) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    // Redirigir al usuario a la pantalla principal o dashboard
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtalo de nuevo.");
                }
            }
        });
    }
}