package database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class OwnerDAO {

    // Metodo para obtener los datos del owner por user_id
    public static Map<String, String> getOwnerByUserId(int userId) {
        String query = "SELECT dni_number, birth_date, address, phone_number FROM owners WHERE user_id = ?";
        Map<String, String> ownerData = new HashMap<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ownerData.put("dni_number", rs.getString("dni_number"));
                ownerData.put("birth_date", rs.getString("birth_date"));
                ownerData.put("address", rs.getString("address"));
                ownerData.put("phone_number", rs.getString("phone_number"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos del owner: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return ownerData;
    }

    // Metodo para actualizar los datos del owner
    public static boolean updateOwnerProfile(int userId, String dniNumber, String birthDate, String address, String phoneNumber) {
        String query = "UPDATE owners SET dni_number = ?, birth_date = ?, address = ?, phone_number = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, dniNumber);
            stmt.setString(2, birthDate);
            stmt.setString(3, address);
            stmt.setString(4, phoneNumber);
            stmt.setInt(5, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó correctamente
        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil del owner: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return false;
    }
}