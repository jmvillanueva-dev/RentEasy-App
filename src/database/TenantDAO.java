// TenantDAO.java
package database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TenantDAO {

    // Metodo para obtener los datos del tenant por user_id
    public static Map<String, String> getTenantByUserId(int userId) {
        String query = "SELECT dni_number, city, region, address, card_number, card_deadline, cvc_number FROM tenants WHERE user_id = ?";
        Map<String, String> tenantData = new HashMap<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenantData.put("dni_number", rs.getString("dni_number"));
                tenantData.put("city", rs.getString("city"));
                tenantData.put("region", rs.getString("region"));
                tenantData.put("address", rs.getString("address"));
                tenantData.put("card_number", rs.getString("card_number"));
                tenantData.put("card_deadline", rs.getString("card_deadline"));
                tenantData.put("cvc_number", rs.getString("cvc_number"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos del tenant: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return tenantData;
    }

    // Metodo para actualizar los datos del tenant
    public static boolean updateTenantProfile(int userId, String dniNumber, String city, String region, String address, String cardNumber, String cardDeadline, String cvcNumber) {
        String query = "UPDATE tenants SET dni_number = ?, city = ?, region = ?, address = ?, card_number = ?, card_deadline = ?, cvc_number = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, dniNumber);
            stmt.setString(2, city);
            stmt.setString(3, region);
            stmt.setString(4, address);
            stmt.setString(5, cardNumber);
            stmt.setString(6, cardDeadline);
            stmt.setString(7, cvcNumber);
            stmt.setInt(8, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó correctamente
        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil del tenant: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return false;
    }

    // Metodo para obtener la información del propietario de una propiedad
    public static Map<String, String> getOwnerInfoByPropertyId(int propertyId) {
        String query = "SELECT u.user_name, u.user_lastname, u.email, o.phone_number FROM users u " +
                "JOIN owners o ON u.id = o.user_id " +
                "JOIN properties p ON o.user_id = p.owner_id " +
                "WHERE p.id = ?";
        Map<String, String> ownerInfo = new HashMap<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, propertyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ownerInfo.put("name", rs.getString("user_name"));
                ownerInfo.put("lastname", rs.getString("user_lastname"));
                ownerInfo.put("email", rs.getString("email"));
                ownerInfo.put("phone", rs.getString("phone_number"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener información del propietario: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return ownerInfo;
    }
}