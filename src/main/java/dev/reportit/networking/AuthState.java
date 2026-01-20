package dev.reportit.networking;

public class AuthState {
    private static String jwt;

    public static void setToken(String token) {
        jwt = token;
        System.out.println("Token: " + token);
    }

    public static String getToken() {
        return jwt;
    }

    public static boolean isAuthenticated() {
        return jwt != null;
    }

    public static void clear() {
        jwt = null;
    }
}
