package views;

import database.ReservationsDAO;
import database.PropertyDAO;
import utils.SessionManager;
import models.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class BookingDialog extends JDialog {
    private JComboBox<String> propertyComboBox; // Para seleccionar la propiedad
    private JSpinner startDateSpinner; // Fecha de inicio
    private JSpinner endDateSpinner; // Fecha de fin
    private Map<Integer, Integer> propertyOwnerMap; // Mapeo de property_id a owner_id

    public BookingDialog(JFrame parent) {
        super(parent, "Crear Reserva", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Obtener el ID del usuario logeado (tenant)
        int tenantId = SessionManager.getUserId();

        // Cargar propiedades disponibles desde la base de datos
        PropertyDAO propertyDAO = new PropertyDAO();
        List<Property> properties = propertyDAO.getAllProperties();
        Map<Integer, Integer> propertyOwnerMap = new HashMap<>();
        DefaultComboBoxModel<String> propertyModel = new DefaultComboBoxModel<>();

        for (Property property : properties) {
            propertyModel.addElement(property.getTitle()); // Mostrar el título de la propiedad
            propertyOwnerMap.put(property.getId(), property.getOwnerId()); // Guardar el mapeo property_id -> owner_id
        }

        propertyComboBox = new JComboBox<>(propertyModel);

        // Fechas
        SpinnerDateModel startDateModel = new SpinnerDateModel(new Date(), new Date(), null, Calendar.DAY_OF_MONTH);
        SpinnerDateModel endDateModel = new SpinnerDateModel(new Date(), new Date(), null, Calendar.DAY_OF_MONTH);
        startDateSpinner = new JSpinner(startDateModel);
        endDateSpinner = new JSpinner(endDateModel);

        // Formato de fecha para los spinners
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);
        endDateSpinner.setEditor(endDateEditor);

        // Configuración de etiquetas y campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        addLabelAndField(gbc, "Propiedad:", propertyComboBox);
        gbc.gridy++;
        addLabelAndField(gbc, "Fecha de Inicio:", startDateSpinner);
        gbc.gridy++;
        addLabelAndField(gbc, "Fecha de Fin:", endDateSpinner);

        // Botón Guardar Reserva
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Guardar Reserva");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores seleccionados
                String selectedPropertyTitle = (String) propertyComboBox.getSelectedItem();
                int propertyId = getPropertyIdByTitle(selectedPropertyTitle); // Obtener el ID de la propiedad seleccionada
                int ownerId = propertyOwnerMap.get(propertyId); // Obtener el owner_id correspondiente
                Date startDate = (Date) startDateSpinner.getValue();
                Date endDate = (Date) endDateSpinner.getValue();

                // Validar que la fecha de inicio sea posterior a la fecha actual
                if (startDate.before(new Date())) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "La fecha de inicio debe ser posterior a la fecha actual.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que la fecha de fin sea posterior a la fecha de inicio
                if (endDate.before(startDate)) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "La fecha de fin debe ser posterior a la fecha de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Formatear las fechas como texto
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startDateStr = dateFormat.format(startDate);
                String endDateStr = dateFormat.format(endDate);

                // Insertar la reserva en la base de datos
                boolean success = ReservationsDAO.createReservation(tenantId, ownerId, propertyId, startDateStr, endDateStr);
                if (success) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "Reserva creada exitosamente.");
                    dispose(); // Cerrar el diálogo
                } else {
                    JOptionPane.showMessageDialog(BookingDialog.this, "Error al crear la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton, gbc);
        setVisible(true);
    }

    // Método para obtener el ID de una propiedad por su título
    private int getPropertyIdByTitle(String title) {
        PropertyDAO propertyDAO = new PropertyDAO();
        List<Property> properties = propertyDAO.getAllProperties();
        for (Property property : properties) {
            if (property.getTitle().equals(title)) {
                return property.getId();
            }
        }
        return -1; // Propiedad no encontrada
    }

    // Método auxiliar para agregar una etiqueta y un campo
    private void addLabelAndField(GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridwidth = 1; // Una columna para la etiqueta
        gbc.anchor = GridBagConstraints.LINE_END; // Alinear a la derecha
        add(new JLabel(labelText), gbc);
        gbc.gridx++; // Mover a la siguiente columna
        gbc.anchor = GridBagConstraints.LINE_START; // Alinear a la izquierda
        add(field, gbc);
        gbc.gridx = 0; // Volver a la primera columna
        gbc.gridy++; // Mover a la siguiente fila
    }

    // Metodo para obtener el título de una propiedad por su ID
    private String getPropertyTitleById(int propertyId) {
        // Aquí podrías implementar una consulta a la base de datos si es necesario
        return propertyComboBox.getItemAt(propertyComboBox.getSelectedIndex());
    }
}