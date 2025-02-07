import database.UserDAO;
import models.User;
import utils.PlaceholderUtil;
import utils.WindowManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JRadioButton ownerRadioButton;
    private JRadioButton tenantRadioButton;
    private JCheckBox acceptTermsCheckBox;
    private JCheckBox acceptPolicyDeCheckBox;
    private JButton createCountButton;
    private JButton goLoginButton;
    private JButton backPannelbutton;
    private JLabel resultVertification;
    private JLabel resultVertification2;

    public Register() {

        PlaceholderUtil.setPlaceholder(userName, "Ingrese su nombre*");
        PlaceholderUtil.setPlaceholder(userLastname, "Ingrese su apellido*");
        PlaceholderUtil.setPlaceholder(userEmail, "Ingrese su e-mail*");
        PlaceholderUtil.setPlaceholder(userPassword, "Crear Contraseña");
        PlaceholderUtil.setPlaceholder(userPassConf, "Crear Constraseña");

        // Validación en tiempo real de las contraseñas
        DocumentListener passwordListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validatePasswords();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validatePasswords();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validatePasswords();
            }
        };

        userPassword.getDocument().addDocumentListener(passwordListener);
        userPassConf.getDocument().addDocumentListener(passwordListener);

        // Navegación a la ventana de Login
        goLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(registerPanel);
                WindowManager.navigateTo(currentFrame, new Login().loginPanel, "RentEasy | Inicio de Sesión");
            }
        });

        // Navegación a la ventana de Bienvenida
        backPannelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(registerPanel);
                WindowManager.navigateTo(currentFrame, new Welcome().welcomePanel, "RentEasy | Bienvenido");
            }
        });

        // Lógica de registro
        createCountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFieldEmpty(userName, "Ingrese su nombre*") ||
                        isFieldEmpty(userLastname, "Ingrese su apellido*") ||
                        isFieldEmpty(userEmail, "Ingrese su e-mail*") ||
                        isFieldEmpty(userPassword, "Crear Contraseña") ||
                        isFieldEmpty(userPassConf, "Crear Constraseña")) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                if (!ownerRadioButton.isSelected() && !tenantRadioButton.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione al menos una actividad.");
                    return;
                }

                if (!acceptTermsCheckBox.isSelected() || !acceptPolicyDeCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Por favor, acepte los términos y la política de privacidad.");
                    return;
                }

                if (!new String(userPassword.getPassword()).equals(new String(userPassConf.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
                    return;
                }

                UserDAO userDAO = new UserDAO();
                User newUser = new User(
                        userName.getText(),
                        userLastname.getText(),
                        userEmail.getText(),
                        new String(userPassword.getPassword()),
                        true,
                        true
                );
                newUser.setOwner(ownerRadioButton.isSelected());
                newUser.setTenant(tenantRadioButton.isSelected());
                boolean registrationSuccess = userDAO.insertUser(newUser);

                if (registrationSuccess) {
                    JOptionPane.showMessageDialog(null, "Te has registrado con éxito, ahora puedes usar nuestros servicios.");
                    resetFormFields();
                } else {
                    JOptionPane.showMessageDialog(null, "El correo electrónico ya está registrado. Por favor, utiliza otro.");
                }
            }
        });
    }

    // Metodo para validar si un campo está vacío o contiene el placeholder
    private boolean isFieldEmpty(JTextField field, String placeholder) {
        String text = field.getText();
        return text.isEmpty() || text.equals(placeholder);
    }

    private boolean isFieldEmpty(JPasswordField field, String placeholder) {
        String text = new String(field.getPassword());
        return text.isEmpty() || text.equals(placeholder);
    }

    // Metodo para validar las contraseñas en tiempo real
    private void validatePasswords() {
        String password = new String(userPassword.getPassword());
        String confirmPassword = new String(userPassConf.getPassword());
        Color greenCustom = Color.decode("#8FD14F");

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            resultVertification.setText("");
            resultVertification2.setText("");
        } else if (password.equals(confirmPassword)) {
            resultVertification.setText("OK");
            resultVertification.setForeground(greenCustom);
            resultVertification2.setText("OK");
            resultVertification2.setForeground(greenCustom);
        } else {
            resultVertification.setText("X");
            resultVertification.setForeground(Color.RED);
            resultVertification2.setText("X");
            resultVertification2.setForeground(Color.RED);
        }
    }

    private void resetFormFields() {
        userName.setText("");
        userLastname.setText("");
        userEmail.setText("");
        userPassword.setText("");
        userPassConf.setText("");
        resultVertification.setText("");
        resultVertification2.setText("");
        ownerRadioButton.setSelected(false);
        tenantRadioButton.setSelected(false);
        acceptTermsCheckBox.setSelected(false);
        acceptPolicyDeCheckBox.setSelected(false);

        PlaceholderUtil.setPlaceholder(userName, "Ingrese su nombre*");
        PlaceholderUtil.setPlaceholder(userLastname, "Ingrese su apellido*");
        PlaceholderUtil.setPlaceholder(userEmail, "Ingrese su e-mail*");
        PlaceholderUtil.setPlaceholder(userPassword, "Crear Contraseña*");
        PlaceholderUtil.setPlaceholder(userPassConf, "Crear Constraseña*");
    }
}