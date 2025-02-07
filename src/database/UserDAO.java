package database;

import models.User;
import utils.PasswordUtil;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    // Metodo para verificar si el correo ya está registrado:
    public boolean isEmailAlreadyRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el correo ya está registrado
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return false;
    }

    private void assignRoles(int userId, User user) {
        String roleQuery = "INSERT INTO user_roles (userId, role_assignment) VALUES (?, ?)";
        String ownerQuery = "INSERT INTO owners (user_id) VALUES (?)";
        String tenantQuery = "INSERT INTO tenants (user_id, city, region, address, card_number, card_deadline, cvc_number) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement roleStmt = connection.prepareStatement(roleQuery);
                PreparedStatement ownerStmt = connection.prepareStatement(ownerQuery);
                PreparedStatement tenantStmt = connection.prepareStatement(tenantQuery)
        ) {
            String roleAssignment = "";
            if (user.isOwner() && user.isTenant()) {
                roleAssignment = "both";
            } else if (user.isOwner()) {
                roleAssignment = "owner";
            } else if (user.isTenant()) {
                roleAssignment = "tenant";
            }

            // Insertar registro en la tabla user_roles
            roleStmt.setInt(1, userId);
            roleStmt.setString(2, roleAssignment);
            roleStmt.executeUpdate();

            // Registrar al usuario como owner si es propietario
            if (user.isOwner()) {
                ownerStmt.setInt(1, userId);
                ownerStmt.executeUpdate();
            }

            //Registrar al usuario en tenant si es arrendatario
            if (user.isTenant()) {
                tenantStmt.setInt(1, userId);
                tenantStmt.setString(2, ""); // city (vacío por ahora)
                tenantStmt.setString(3, ""); // region (vacío por ahora)
                tenantStmt.setString(4, ""); // address (vacío por ahora)
                tenantStmt.setString(5, ""); // card_number (vacío por ahora)
                tenantStmt.setString(6, ""); // card_deadline (vacío por ahora)
                tenantStmt.setString(7, ""); // cvc_number (vacío por ahora)
                tenantStmt.executeUpdate();
            }

            System.out.println("Roles asignados correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al asignar roles: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    // Metodo para agregar un nuevo usuario a la DB
    public boolean insertUser(User user) {
        if (isEmailAlreadyRegistered(user.getEmail())) {
            System.out.println("El correo ya está registrado.");
            return false; // Indicamos que el registro falló debido a un correo duplicado
        }

        String query = "INSERT INTO users (user_name, user_lastname, email, password, accepted_privacy_policy, accepted_terms_and_conditions) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, PasswordUtil.hashPassword(user.getPassword()));
            stmt.setInt(5, user.isAcceptedPrivacyPolicy() ? 1 : 0);
            stmt.setInt(6, user.isAcceptedTermsAndConditions() ? 1 : 0);
            stmt.executeUpdate();

            // Obtener el ID del usuario recién insertado
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Insertar en las tablas owners/tenants según los roles seleccionados
                assignRoles(userId, user);
            }

            System.out.println("Se ha agregado el usuario a la base de datos");
            return true; // Indicamos que el registro fue exitoso
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return false; // En caso de error
    }

    // Metodo para validar credenciales durante el login
    public int validateCredentialsAndGetId(String email, String password) {
        String query = "SELECT id, password FROM users WHERE email = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    return rs.getInt("id"); // Guardar user_id
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return -1;
    }

    // Metodo para obtener el rol de usuario
    public String getUserRole(int userId) {
        String query = "SELECT role_assignment FROM user_roles WHERE userId = ?";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role_assignment");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el rol del usuario: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return null; // Retorna null si no se encuentra el rol
    }

    public Map<String, String> getOwnerProfile(int userId) {
        Map<String, String> profileData = new HashMap<>();
        String query = "SELECT name, lastname, phone_number FROM owners WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profileData.put("name", rs.getString("name"));
                profileData.put("lastname", rs.getString("lastname"));
                profileData.put("phone_number", rs.getString("phone_number"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el perfil del propietario: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
        return profileData;
    }
}

