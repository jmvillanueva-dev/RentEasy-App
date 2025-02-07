import database.UserDAO;
import utils.WindowManager;
import utils.SessionManager;

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
                int userId = userDAO.validateCredentialsAndGetId(email, password); // Obtener el ID del usuario
                if (userId != -1) {
                    SessionManager.setUserId(userId); // Almacenar el ID del usuario en SessionManager
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                    // Redirigir al usuario a la pantalla principal
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);
                    WindowManager.navigateTo(currentFrame, new MainPanel().mainPanel, "RentEasy App");
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtalo de nuevo.");
                }
            }
        });
    }
}