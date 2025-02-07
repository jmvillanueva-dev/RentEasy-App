package views;

import database.OwnerDAO;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class EditProfileDialog extends JDialog {
    private JTextField dniField;
    private JTextField birthDateField;
    private JTextField addressField;
    private JTextField phoneField;

    public EditProfileDialog(JFrame parent) {
        super(parent, "Editar Perfil", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Obtener el ID del usuario logeado
        int userId = SessionManager.getUserId();

        // Cargar datos actuales del owner
        Map<String, String> ownerData = OwnerDAO.getOwnerByUserId(userId);
        String currentDni = ownerData.getOrDefault("dni_number", "");
        String currentBirthDate = ownerData.getOrDefault("birth_date", "");
        String currentAddress = ownerData.getOrDefault("address", "");
        String currentPhone = ownerData.getOrDefault("phone_number", "");

        // Configuración de etiquetas y campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        addLabelAndField(gbc, "Cédula de Indentidad:", dniField = new JTextField(currentDni));

        gbc.gridy++;
        addLabelAndField(gbc, "Fecha de Nacimiento:", birthDateField = new JTextField(currentBirthDate));

        gbc.gridy++;
        addLabelAndField(gbc, "Dirección:", addressField = new JTextField(currentAddress));

        gbc.gridy++;
        addLabelAndField(gbc, "Teléfono:", phoneField = new JTextField(currentPhone));

        // Botón Guardar Cambios
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dni = dniField.getText();
                String birthDate = birthDateField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();

                // Actualizar los datos en la base de datos
                boolean success = OwnerDAO.updateOwnerProfile(userId, dni, birthDate, address, phone);
                if (success) {
                    JOptionPane.showMessageDialog(EditProfileDialog.this, "Perfil actualizado exitosamente.");
                    dispose(); // Cerrar el diálogo
                } else {
                    JOptionPane.showMessageDialog(EditProfileDialog.this, "Error al actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton, gbc);

        setVisible(true);
    }

    // Metodo auxiliar para agregar una etiqueta y un campo
    private void addLabelAndField(GridBagConstraints gbc, String labelText, JTextField field) {
        gbc.gridwidth = 1; // Una columna para la etiqueta
        gbc.anchor = GridBagConstraints.LINE_END; // Alinear a la derecha
        add(new JLabel(labelText), gbc);

        gbc.gridx++; // Mover a la siguiente columna
        gbc.anchor = GridBagConstraints.LINE_START; // Alinear a la izquierda
        field.setColumns(20); // Ancho del campo
        add(field, gbc);

        gbc.gridx = 0; // Volver a la primera columna
        gbc.gridy++; // Mover a la siguiente fila
    }
}