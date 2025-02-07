package views;

import models.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditPropertyDialog extends JDialog {
    private JTextField titleField;
    private JComboBox<String> countryComboBox;
    private JComboBox<String> cityComboBox;
    private JComboBox<String> typeComboBox;
    private JTextField priceField;
    private JSpinner roomsSpinner;
    private JSpinner maxPeopleSpinner;
    private JTextField addressField;
    private JButton saveButton;
    private JButton cancelButton;
    private Property property;
    private boolean saved = false;

    public EditPropertyDialog(JFrame parent, Property property) {
        super(parent, "Editar Propiedad", true);
        this.property = property;
        setSize(400, 500); // Aumentamos el tamaño para acomodar mejor los campos
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.WEST; // Alineación al oeste (izquierda)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente

        // Campos del formulario
        titleField = new JTextField(property.getTitle(), 20);
        countryComboBox = new JComboBox<>(new String[]{"Ecuador", "Colombia", "Perú"});
        countryComboBox.setSelectedItem(property.getCountry());
        cityComboBox = new JComboBox<>(new String[]{"Quito", "Guayaquil", "Cuenca", "Manta", "Ambato", "Esmeraldas", "Portoviejo", "Pedernales", "Montañita", "Lago Agrio", "Macas"});
        cityComboBox.setSelectedItem(property.getCity());
        typeComboBox = new JComboBox<>(new String[]{"Habitación", "Casa", "Departamento", "Cabaña"});
        typeComboBox.setSelectedItem(property.getPropertyType());
        priceField = new JTextField(String.valueOf(property.getPrice()), 20);
        roomsSpinner = new JSpinner(new SpinnerNumberModel(property.getRooms(), 1, 10, 1));
        maxPeopleSpinner = new JSpinner(new SpinnerNumberModel(property.getMaxPeople(), 1, 10, 1));
        addressField = new JTextField(property.getAddress(), 20);

        // Botones
        saveButton = new JButton("Guardar");
        cancelButton = new JButton("Cancelar");

        // Agregar componentes al panel con GridBagLayout
        int row = 0;
        addLabelAndComponent(panel, gbc, "Título:", titleField, row++);
        addLabelAndComponent(panel, gbc, "País:", countryComboBox, row++);
        addLabelAndComponent(panel, gbc, "Ciudad:", cityComboBox, row++);
        addLabelAndComponent(panel, gbc, "Tipo:", typeComboBox, row++);
        addLabelAndComponent(panel, gbc, "Precio por noche:", priceField, row++);
        addLabelAndComponent(panel, gbc, "Habitaciones:", roomsSpinner, row++);
        addLabelAndComponent(panel, gbc, "Máximo de personas:", maxPeopleSpinner, row++);
        addLabelAndComponent(panel, gbc, "Dirección:", addressField, row++);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Agregar el panel de botones al final
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Centrar los botones
        panel.add(buttonPanel, gbc);

        add(panel);

        // Acción del botón Guardar
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar la propiedad con los nuevos valores
                property.setTitle(titleField.getText());
                property.setCountry((String) countryComboBox.getSelectedItem());
                property.setCity((String) cityComboBox.getSelectedItem());
                property.setPropertyType((String) typeComboBox.getSelectedItem());
                property.setPrice(Double.parseDouble(priceField.getText()));
                property.setRooms((int) roomsSpinner.getValue());
                property.setMaxPeople((int) maxPeopleSpinner.getValue());
                property.setAddress(addressField.getText());
                saved = true;
                dispose();
            }
        });

        // Acción del botón Cancelar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Método auxiliar para agregar etiquetas y componentes al panel
    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST; // Alinear la etiqueta a la derecha
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Alinear el componente a la izquierda
        panel.add(component, gbc);
    }

    public boolean isSaved() {
        return saved;
    }

    public Property getUpdatedProperty() {
        return property;
    }
}