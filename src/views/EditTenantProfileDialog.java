package views;

import database.TenantDAO;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class EditTenantProfileDialog extends JDialog {
    private JTextField dniField;
    private JTextField cityField;
    private JTextField regionField;
    private JTextField addressField;
    private JTextField cardNumberField;
    private JTextField cardDeadlineField;
    private JTextField cvcField;

    public EditTenantProfileDialog(JFrame parent) {
        super(parent, "Editar Perfil de Arrendatario", true);
        setSize(400, 450); // Ajusta el tamaño para incluir más campos
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Obtener el ID del usuario logeado
        int userId = SessionManager.getUserId();

        // Cargar datos actuales del tenant
        Map<String, String> tenantData = TenantDAO.getTenantByUserId(userId);
        String currentDni = tenantData.getOrDefault("dni_number", "");
        String currentCity = tenantData.getOrDefault("city", "");
        String currentRegion = tenantData.getOrDefault("region", "");
        String currentAddress = tenantData.getOrDefault("address", "");
        String currentCardNumber = tenantData.getOrDefault("card_number", "");
        String currentCardDeadline = tenantData.getOrDefault("card_deadline", "");
        String currentCvc = tenantData.getOrDefault("cvc_number", "");

        // Configuración de etiquetas y campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        addLabelAndField(gbc, "Cédula de Identidad:", dniField = new JTextField(currentDni));
        gbc.gridy++;
        addLabelAndField(gbc, "Ciudad:", cityField = new JTextField(currentCity));
        gbc.gridy++;
        addLabelAndField(gbc, "Región:", regionField = new JTextField(currentRegion));
        gbc.gridy++;
        addLabelAndField(gbc, "Dirección:", addressField = new JTextField(currentAddress));
        gbc.gridy++;
        addLabelAndField(gbc, "Número de Tarjeta:", cardNumberField = new JTextField(currentCardNumber));
        gbc.gridy++;
        addLabelAndField(gbc, "Fecha de Vencimiento:", cardDeadlineField = new JTextField(currentCardDeadline));
        gbc.gridy++;
        addLabelAndField(gbc, "CVC:", cvcField = new JTextField(currentCvc));

        // Botón Guardar Cambios
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dni = dniField.getText();
                String city = cityField.getText();
                String region = regionField.getText();
                String address = addressField.getText();
                String cardNumber = cardNumberField.getText();
                String cardDeadline = cardDeadlineField.getText();
                String cvc = cvcField.getText();

                // Actualizar los datos en la base de datos
                boolean success = TenantDAO.updateTenantProfile(userId, dni, city, region, address, cardNumber, cardDeadline, cvc);
                if (success) {
                    JOptionPane.showMessageDialog(EditTenantProfileDialog.this, "Perfil actualizado exitosamente.");
                    dispose(); // Cerrar el diálogo
                } else {
                    JOptionPane.showMessageDialog(EditTenantProfileDialog.this, "Error al actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
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