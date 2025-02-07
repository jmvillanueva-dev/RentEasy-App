package utils;

public class SessionManager {
    private static int userId = -1; // -1 indica que no hay usuario autenticado

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static boolean isUserLoggedIn() {
        return userId != -1;
    }

    public static void logout() {
        userId = -1;
    }
}