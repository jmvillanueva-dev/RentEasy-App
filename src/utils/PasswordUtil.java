package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Metodo para hashear una contraseña
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Metodo para verificar si una contraseña coincide con su hash
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}