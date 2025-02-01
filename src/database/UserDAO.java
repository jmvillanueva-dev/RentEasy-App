package database;

import models.User;
import java.sql.*;

public class UserDAO {

    public void insertUser(User user) {

        String query = "INSERT INTO users (user_name, user_lastname, email, password, accepted_privacy_policy, accepted_terms_and_conditions) VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1,user.getUsername());
            stmt.setString(2, user.getLastname() );
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.isAcceptedPrivacyPolicy() ? 1 : 0);
            stmt.setInt(6, user.isAcceptedTermsAndConditions() ? 1 : 0);
            stmt.executeUpdate();
            System.out.println("Se ha agregado el usuario a la base de datos");

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}