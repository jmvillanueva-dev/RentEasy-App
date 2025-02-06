package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:./src/SQLiteDatabase/db_renteasy_app.db";
    private static Connection connection = null;

    // Constructor privado para evitar instancias
    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                System.out.println("Conectado a la base de datos: " + connection.getMetaData().getURL());
                System.out.println("Conexión a SQLite establecida.");
            } catch (SQLException e) {
                System.err.println("Error al conectar a SQLite: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                System.out.println("Conexión a SQLite cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
