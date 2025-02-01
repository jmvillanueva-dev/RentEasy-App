import database.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register {
    public JPanel registerPanel;
    private JTextField userName;
    private JTextField userLastname;
    private JTextField userEmail;
    private JPasswordField userPassword;
    private JPasswordField userPassConf;
    private JRadioButton propietarioRadioButton;
    private JRadioButton arrendatarioRadioButton;
    private JCheckBox aceptoLosTérminosYCheckBox;
    private JCheckBox aceptoLaPolíticaDeCheckBox;
    private JButton createCountButton;
    private JButton goLoginButton;


    public Register() {

        FocusUtil.disableAutoFocus(userName, userLastname, userEmail, userPassword, userPassConf);
        PlaceholderUtil.setPlaceholder(userName, "Ingrese su nombre");
        PlaceholderUtil.setPlaceholder(userLastname, "Ingrese su apellido");
        PlaceholderUtil.setPlaceholder(userEmail, "Ingrese su e-mail");
        PlaceholderUtil.setPlaceholder(userPassword, "Crear Contraseña");
        PlaceholderUtil.setPlaceholder(userPassConf, "Crear Constraseña");

        goLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(registerPanel);

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

        createCountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UserDAO userDAO = new UserDAO();
                User newUser = new User(
                        userName.getText(),
                        userLastname.getText(),
                        userEmail.getText(),
                        new String(userPassword.getPassword()),
                        true,
                        true
                );
                userDAO.insertUser(newUser);
                JOptionPane.showMessageDialog(null, "Tu registro se ha completado con éxito");
            }
        });
    }
}
