package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationsDAO {
    public static boolean createReservation(int tenantId, int ownerId, int propertyId, String startDate, String endDate) {
        String query = "INSERT INTO reservations (tenant_id, owner_id, property_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tenantId);
            stmt.setInt(2, ownerId);
            stmt.setInt(3, propertyId);
            stmt.setString(4, startDate);
            stmt.setString(5, endDate);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}