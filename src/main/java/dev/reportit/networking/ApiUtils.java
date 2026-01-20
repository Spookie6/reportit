package dev.reportit.networking;

import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ApiUtils {

    protected static final String BASE_URL = "https://reportit.dev/";

    public static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static final HttpClient client = HttpClient.newBuilder().executor(executor).build();

    public static void sendAsync(HttpRequest req, Consumer<String> onSuccess, Consumer<Throwable> onError) {
        client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response ->
                        Platform.runLater(() -> {
                            onSuccess.accept(response.body());
                        })
                )
                .exceptionally(ex -> {
                    Platform.runLater(() -> onError.accept(ex));
                    return null;
                });
    }
}
