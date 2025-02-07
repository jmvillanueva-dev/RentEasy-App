package database;

import models.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PropertyDAO {

    // Metodo para obtener las propiedades de un usuario
    public List<Property> getPropertiesByOwnerId(int ownerId) {
        List<Property> properties = new ArrayList<>();
        String query = "SELECT * FROM properties WHERE owner_id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Property property = new Property(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner_id"),
                        resultSet.getString("title"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getDouble("price"),
                        resultSet.getString("property_type"),
                        resultSet.getInt("rooms"),
                        resultSet.getInt("max_people"),
                        resultSet.getString("address")
                );
                properties.add(property);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las propiedades: " + e.getMessage());
        }finally {
            DatabaseConnection.closeConnection();
        }
        return properties;
    }

    public boolean registerProperty(Property property) {
        String query = "INSERT INTO properties (owner_id, title, city, country, price, property_type, rooms, max_people, address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, property.getOwnerId());
            statement.setString(2, property.getTitle());
            statement.setString(3, property.getCity());
            statement.setString(4, property.getCountry());
            statement.setDouble(5, property.getPrice());
            statement.setString(6, property.getPropertyType());
            statement.setInt(7, property.getRooms());
            statement.setInt(8, property.getMaxPeople());
            statement.setString(9, property.getAddress());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar la propiedad: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    // Metodo para recuperar una propiedad
    public Property getPropertyById(int propertyId) {
        String query = "SELECT * FROM properties WHERE id = ?";
        Property property = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, propertyId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                property = new Property(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner_id"),
                        resultSet.getString("title"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getDouble("price"),
                        resultSet.getString("property_type"),
                        resultSet.getInt("rooms"),
                        resultSet.getInt("max_people"),
                        resultSet.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la propiedad: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }

        return property;
    }

    // Metodo para editar una propiedad existente
    public boolean updateProperty(Property property) {
        String query = "UPDATE properties SET title = ?, city = ?, country = ?, price = ?, property_type = ?, rooms = ?, max_people = ?, address = ? WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, property.getTitle());
            statement.setString(2, property.getCity());
            statement.setString(3, property.getCountry());
            statement.setDouble(4, property.getPrice());
            statement.setString(5, property.getPropertyType());
            statement.setInt(6, property.getRooms());
            statement.setInt(7, property.getMaxPeople());
            statement.setString(8, property.getAddress());
            statement.setInt(9, property.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la propiedad: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    // Metodo para eliminar una propiedad
    public boolean deleteProperty(int propertyId) {
        String query = "DELETE FROM properties WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, propertyId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la propiedad: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    public List<Property> filterProperties(String minPrice, String maxPrice, String city, String propertyType) {
        List<Property> filteredProperties = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM properties WHERE ");
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (!minPrice.equals("0")) {
            conditions.add("price >= ?");
            parameters.add(Double.parseDouble(minPrice));
        }
        if (!maxPrice.equals("99999999")) {
            conditions.add("price <= ?");
            parameters.add(Double.parseDouble(maxPrice));
        }
        if (!city.isEmpty()) {
            conditions.add("city LIKE ?");
            parameters.add("%" + city + "%");
        }
        if (!propertyType.isEmpty()) {
            conditions.add("property_type LIKE ?");
            parameters.add("%" + propertyType + "%");
        }

        // Si no hay condiciones, devolver todo sin filtrar
        if (conditions.isEmpty()) {
            queryBuilder.append("1=1"); // Selecciona todas las propiedades
        } else {
            queryBuilder.append(String.join(" OR ", conditions)); // Usa OR para filtrar al menos por un criterio
        }

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())
        ) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Property property = new Property(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner_id"),
                        resultSet.getString("title"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getDouble("price"),
                        resultSet.getString("property_type"),
                        resultSet.getInt("rooms"),
                        resultSet.getInt("max_people"),
                        resultSet.getString("address")
                );
                filteredProperties.add(property);
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar las propiedades: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return filteredProperties;
    }


    public List<Property> getAllProperties() {
        List<Property> allProperties = new ArrayList<>();
        String query = "SELECT * FROM properties";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Property property = new Property(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner_id"),
                        resultSet.getString("title"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getDouble("price"),
                        resultSet.getString("property_type"),
                        resultSet.getInt("rooms"),
                        resultSet.getInt("max_people"),
                        resultSet.getString("address")
                );
                allProperties.add(property);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las propiedades: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return allProperties;
    }



}