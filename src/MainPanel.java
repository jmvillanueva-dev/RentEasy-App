import models.Property;
import utils.SessionManager;
import database.PropertyDAO;
import database.UserDAO;
import database.TenantDAO;
import utils.WindowManager;
import views.EditPropertyDialog;
import views.EditProfileDialog;
import views.EditTenantProfileDialog;
import  views.BookingDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class MainPanel {
    public JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel owenerPanel;
    private JPanel tenantPanel;
    private JPanel owPropiedades;
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
    private JPanel ownersPanel;
    private JPanel tenantsPanel;
    private JTable showProperties;
    private JButton searchButton;
    private JButton bookingButton;
    private JButton editProfileTenant;
    private JLabel userNameLabel;
    private JTextField minPrice;
    private JComboBox typeFilterCBox;
    private JTextField maxPrice;
    private JButton infoOwnerButton;
    private JComboBox cityFilterCBox;
    private JButton cleanFiltroButton;

    DefaultTableModel modelForOwner = new DefaultTableModel(
            new String[]{"Code","Título", "Ciudad", "País", "Precio Noche", "Tipo", "Habitaciones", "Cant. Personas", "Dirección"}, 0);

    DefaultTableModel modelForTenants = new DefaultTableModel(
            new String[]{"Code","Título", "Ciudad", "País", "Precio Noche", "Tipo", "Habitaciones", "Cant. Personas", "Dirección"}, 0);

    public MainPanel() {

        // Configurar tablas
        setupTable(seeProperties, modelForOwner);
        setupTable(showProperties, modelForTenants);

        // Gestionar roles y ventanas
        UserDAO userDAO = new UserDAO();
        String userRole = userDAO.getUserRole(SessionManager.getUserId());

        // Mostrar u ocultar paneles según el rol
        if (userRole != null) {
            switch (userRole) {
                case "owner":
                    tabbedPane.remove(tenantsPanel);
                    break;
                case "tenant":
                    tabbedPane.remove(ownersPanel);
                    break;
                case "both":
                    // No se elimina ningún panel, se muestran ambos
                    break;
                default:
                    // En caso de un rol no reconocido, se pueden ocultar ambos paneles
                    tabbedPane.remove(ownersPanel);
                    tabbedPane.remove(tenantsPanel);
                    break;
            }
        } else {
            // Si no se encuentra el rol, ocultar ambos paneles
            tabbedPane.remove(ownersPanel);
            tabbedPane.remove(tenantsPanel);
        }

        //
        loadUserProperties();

        registerProperty.setEnabled(false);
        setupFieldListeners();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SessionManager.logout();
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

        // Editar Perfil Propietario
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new EditProfileDialog(parentFrame);
            }
        });

        // Editar Perfil Arrendatario
        editProfileTenant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // Editar Perfil usuario Panel Arrendatario
        // MainPanel.java
        editProfileTenant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                new EditTenantProfileDialog(parentFrame);
            }
        });

        // Filtrar y buscar Panel Arrendatario
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String minPriceText = minPrice.getText().trim();
                String maxPriceText = maxPrice.getText().trim();
                String city = cityFilterCBox.getSelectedItem() != null ? cityFilterCBox.getSelectedItem().toString().trim() : "";
                String type = typeFilterCBox.getSelectedItem() != null ? typeFilterCBox.getSelectedItem().toString().trim() : "";

                // Asegurar valores predeterminados
                minPriceText = minPriceText.isEmpty() ? "0" : minPriceText;
                maxPriceText = maxPriceText.isEmpty() ? "99999999" : maxPriceText;

                PropertyDAO propertyDAO = new PropertyDAO();
                List<Property> filteredProperties = propertyDAO.filterProperties(minPriceText, maxPriceText, city, type);

                modelForTenants.setRowCount(0);

                for (Property property : filteredProperties) {
                    modelForTenants.addRow(new Object[]{
                            property.getId(),
                            property.getTitle(),
                            property.getCity(),
                            property.getCountry(),
                            property.getPrice(),
                            property.getPropertyType(),
                            property.getRooms(),
                            property.getMaxPeople(),
                            property.getAddress()
                    });
                }
            }
        });

        // Traer la información del propietario Panel Arrendatario

        infoOwnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = showProperties.getSelectedRow();
                if (selectedRow >= 0) {
                    int propertyId = (int) showProperties.getValueAt(selectedRow, 0); // Asumiendo que el ID está en la primera columna

                    Map<String, String> ownerInfo = TenantDAO.getOwnerInfoByPropertyId(propertyId);

                    if (!ownerInfo.isEmpty()) {
                        String message = "Nombre: " + ownerInfo.get("name") + " " + ownerInfo.get("lastname") + "\n" +
                                "Email: " + ownerInfo.get("email") + "\n" +
                                "Teléfono: " + ownerInfo.get("phone");
                        JOptionPane.showMessageDialog(mainPanel, message, "Información del Propietario", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "No se encontró información del propietario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, seleccione una propiedad para ver la información del propietario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        cleanFiltroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minPrice.setText("");
                maxPrice.setText("");
                cityFilterCBox.setSelectedIndex(0);
                typeFilterCBox.setSelectedIndex(0);

                loadUserProperties();

                JOptionPane.showMessageDialog(null, "Filtros eliminados");
            }
        });

        bookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                BookingDialog dialog = new BookingDialog(parentFrame);
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

    private void setupFilterListeners() {
        DocumentListener priceListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilterState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilterState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilterState();
            }
        };

        minPrice.getDocument().addDocumentListener(priceListener);
        maxPrice.getDocument().addDocumentListener(priceListener);

        ActionListener comboListener = e -> updateFilterState();

        cityFilterCBox.addActionListener(comboListener);
        typeFilterCBox.addActionListener(comboListener);
    }

    private void updateFilterState() {
        boolean hasPriceFilter = !minPrice.getText().isEmpty() || !maxPrice.getText().isEmpty();
        boolean hasCityFilter = cityFilterCBox.getSelectedItem() != null && !cityFilterCBox.getSelectedItem().toString().isEmpty();
        boolean hasTypeFilter = typeFilterCBox.getSelectedItem() != null && !typeFilterCBox.getSelectedItem().toString().isEmpty();

        cityFilterCBox.setEnabled(!hasPriceFilter && !hasTypeFilter);
        typeFilterCBox.setEnabled(!hasPriceFilter && !hasCityFilter);
        minPrice.setEnabled(!hasCityFilter && !hasTypeFilter);
        maxPrice.setEnabled(!hasCityFilter && !hasTypeFilter);
    }

    // Metodo para cargar las propiedades en Panel Propietario o Arrendatario
    private void loadUserProperties() {
        PropertyDAO propertyDAO = new PropertyDAO();
        List<Property> properties;

        UserDAO userDAO = new UserDAO();
        String userRole = userDAO.getUserRole(SessionManager.getUserId());

        if (userRole.equals("tenant") || userRole.equals("both")) {
            properties = propertyDAO.getAllProperties(); // Cargar todas las propiedades para tenants
        } else {
            properties = propertyDAO.getPropertiesByOwnerId(SessionManager.getUserId()); // Cargar solo las propiedades del propietario
        }

        modelForOwner.setRowCount(0);
        modelForTenants.setRowCount(0);

        for (Property property : properties) {
            if (userRole.equals("tenant") || userRole.equals("both")) {
                modelForTenants.addRow(new Object[]{
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
            } else {
                modelForOwner.addRow(new Object[]{
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

    // Asignar de nombres de columnas a las JTables
    private void setupTable(JTable table, DefaultTableModel model) {
        table.setModel(model);

        int[] columnWidths = {40, 200, 70, 70, 80, 120, 50, 50, 200};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }


}