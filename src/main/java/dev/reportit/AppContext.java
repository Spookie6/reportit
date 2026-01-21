package dev.reportit;

import dev.reportit.networking.UserData;
import dev.reportit.networking.WebSocketClient;

public class AppContext {
    public static final WebSocketClient socket = new WebSocketClient();
    public static UserData currentUser;

    public static void init() {
        ;
    }

    public static void shutdown() {
        socket.close();
    }

    public static void setCurrentUser(UserData currentUser) {
        AppContext.currentUser = currentUser;
    }

    public static UserData getCurrentUser() {
        return currentUser;
    }
}
