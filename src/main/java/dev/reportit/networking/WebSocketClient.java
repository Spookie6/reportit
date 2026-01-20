package dev.reportit.networking;

import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class WebSocketClient {
    private final String URL = "wss://reportit/ws";

    private WebSocket webSocket;

    public void connect() {
        if (AuthState.getToken() == null || AuthState.getToken().isEmpty()) {
            System.out.println("No token");
            return;
        }

        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .header("Authorisation", "Bearer " + AuthState.getToken())
                .buildAsync(
                        URI.create(URL),
                        new WebSocket.Listener() {

                            @Override
                            public CompletionStage<?> onText(
                                    WebSocket webSocket,
                                    CharSequence data,
                                    boolean last) {

                                Platform.runLater(() -> {
                                    System.out.println("Message: " + data);
                                });

                                return WebSocket.Listener.super
                                        .onText(webSocket, data, last);
                            }
                        })
                .thenAccept(ws -> this.webSocket = ws);
    }

    public void send(String msg) {
        if (webSocket != null) {
            webSocket.sendText(msg, true);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(
                    WebSocket.NORMAL_CLOSURE, "bye"
            );
        }
    }
}
