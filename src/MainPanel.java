import models.Property;
import utils.SessionManager;
import database.PropertyDAO;
import utils.WindowManager;
import views.EditPropertyDialog;
import views.EditProfileDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MainPanel {
    public JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel owenerPanel;
    private JPanel tenantPanel;
    private JTabbedPane owContratos;
    private JPanel owPropiedades;
    private JPanel owNotificaciones;
    private JPanel owPerfil;
    private JTextField titleProperty;
    private JComboBox countryPropertyCbox;
    private JComboBox cityPropertyCbox;
    private JComboBox propertyTypeCbox;
    private JTextField pricePerNight;
    private JSpinner roomsQtySpinner;
    private JSpinner maxPeopleSpinner;
    private JButton registerProperty;
    private JTextField addressProperty;
    private JButton logoutButton;
    private JTable seeProperties;
    private JButton deletePropertyButton;
    private JButton editPropertyButton;
    private JButton editProfileButton;

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"Code","Título", "Ciudad", "País", "Precio Noche", "Tipo", "Habitaciones", "Cant. Personas", "Dirección"}, 0);

    public MainPanel() {
        seeProperties.setModel(model);
        loadUserProperties();

        registerProperty.setEnabled(false);
        setupFieldListeners();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SessionManager.logout(); // Limpiar el ID del usuario
                JOptionPane.showMessageDialog(null, "Gracias por usar RentEasy. Vuelve pronto.");

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
                WindowManager.navigateTo(currentFrame, new Welcome().welcomePanel, "RentEasy | Bienvenido");
            }
        });

        registerProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    Property property = new Property(
                            SessionManager.getUserId(),
                            titleProperty.getText(),
                            cityPropertyCbox.getSelectedItem().toString(),
                            countryPropertyCbox.getSelectedItem().toString(),
                            Double.parseDouble(pricePerNight.getText()),
                            propertyTypeCbox.getSelectedItem().toString(),
                            (int) roomsQtySpinner.getValue(),
                            (int) maxPeopleSpinner.getValue(),
                            addressProperty.getText()
                    );

                    PropertyDAO propertyDAO = new PropertyDAO();
                    boolean success = propertyDAO.registerProperty(property);

                    if (success) {
                        JOptionPane.showMessageDialog(mainPanel, "Felicidades. Haz publicado una nueva propiedad.");
                        loadUserProperties();

                        titleProperty.setText("");
                        cityPropertyCbox.setSelectedIndex(-1);
                        countryPropertyCbox.setSelectedIndex(-1);
                        pricePerNight.setText("");
                        propertyTypeCbox.setSelectedIndex(-1);
                        roomsQtySpinner.setValue(1);
                        maxPeopleSpinner.setValue(1);
                        addressProperty.setText("");

                        registerProperty.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Error al registrar la propiedad.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = seeProperties.getSelectedRow();
                if (selectedRow >= 0) {
                    int propertyId = (int) seeProperties.getValueAt(selectedRow, 0); // Asumiendo que el ID está en la primera columna

                    PropertyDAO propertyDAO = new PropertyDAO();
                    Property property = propertyDAO.getPropertyById(propertyId);

                    if (property != null) {
                        // Abrir el diálogo de edición
                        EditPropertyDialog editDialog = new EditPropertyDialog((JFrame) SwingUtilities.getWindowAncestor(mainPanel), property);
                        editDialog.setVisible(true);

                        // Si el usuario guardó los cambios, actualizar la propiedad en la base de datos
                        if (editDialog.isSaved()) {
                            Property updatedProperty = editDialog.getUpdatedProperty();
                            boolean success = propertyDAO.updateProperty(updatedProperty);

                            if (success) {
                                JOptionPane.showMessageDialog(mainPanel, "Propiedad actualizada correctamente.");
                                loadUserProperties(); // Actualizar la tabla
                            } else {
                                JOptionPane.showMessageDialog(mainPanel, "Error al actualizar la propiedad.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, seleccione una propiedad para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deletePropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = seeProperties.getSelectedRow();
                if (selectedRow >= 0) {
                    int propertyId = (int) seeProperties.getValueAt(selectedRow, 0); // Asumiendo que el ID está en la primera columna

                    int confirm = JOptionPane.showConfirmDialog(mainPanel, "¿Está seguro de que desea eliminar esta propiedad?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        PropertyDAO propertyDAO = new PropertyDAO();
                        boolean success = propertyDAO.deleteProperty(propertyId);

                        if (success) {
                            JOptionPane.showMessageDialog(mainPanel, "Propiedad eliminada correctamente.");
                            loadUserProperties();
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Error al eliminar la propiedad.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, seleccione una propiedad para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new EditProfileDialog(parentFrame);
            }
        });
    }

    // Metodo para validar que todos los campos de la propiedad estén llenos
    private boolean validateFields() {
        try {
            Double.parseDouble(pricePerNight.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        return !titleProperty.getText().isEmpty() &&
                countryPropertyCbox.getSelectedItem() != null &&
                cityPropertyCbox.getSelectedItem() != null &&
                propertyTypeCbox.getSelectedItem() != null &&
                !pricePerNight.getText().isEmpty() &&
                (int) roomsQtySpinner.getValue() > 0 &&
                (int) maxPeopleSpinner.getValue() > 0 &&
                !addressProperty.getText().isEmpty();
    }

    // Metodo para capturar eventos en los campos obligatorios
    private void setupFieldListeners() {
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                registerProperty.setEnabled(validateFields());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                registerProperty.setEnabled(validateFields());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                registerProperty.setEnabled(validateFields());
            }
        };

        titleProperty.getDocument().addDocumentListener(documentListener);
        pricePerNight.getDocument().addDocumentListener(documentListener);
        addressProperty.getDocument().addDocumentListener(documentListener);

        ActionListener fieldListener = e -> registerProperty.setEnabled(validateFields());

        countryPropertyCbox.addActionListener(fieldListener);
        cityPropertyCbox.addActionListener(fieldListener);
        propertyTypeCbox.addActionListener(fieldListener);
        roomsQtySpinner.addChangeListener(e -> registerProperty.setEnabled(validateFields()));
        maxPeopleSpinner.addChangeListener(e -> registerProperty.setEnabled(validateFields()));
    }

    // Metodo para cargar las propiedades del usario en la tabla
    private void loadUserProperties() {
        PropertyDAO propertyDAO = new PropertyDAO();
        List<Property> properties = propertyDAO.getPropertiesByOwnerId(SessionManager.getUserId());

        model.setRowCount(0);

        for (Property property : properties) {
            model.addRow(new Object[]{
                    property.getId(),
                    property.getTitle(),
                    property.getCity(),
                    property.getCountry(),
                    property.getPrice(),
                    property.getPropertyType(),
                    property.getRooms(),
                    property.getMaxPeople(),
                    property.getAddress(),
            });
        }
    }



}