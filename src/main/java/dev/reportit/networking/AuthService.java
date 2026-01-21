package dev.reportit.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.reportit.networking.responses.LoginResponse;
import dev.reportit.networking.responses.MessageResponse;
import dev.reportit.networking.responses.Response;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static dev.reportit.networking.ApiUtils.*;

public class AuthService {

    private static final String BASE_URL = ApiUtils.BASE_URL + "auth";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void register(
            String email,
            String username,
            String password,
            Consumer<String> onSuccess,
            Consumer<Throwable> onError
    ) {
        String json = toJson(new RegisterRequest(email, username, password));
        HttpRequest request = createReq(json, "/register");
        sendAsync(request, onSuccess, onError);
    }

    public static void login(
            String email,
            String password,
            Consumer<Response> onSuccess,
            Consumer<Throwable> onError
    ) {
        String json = toJson(new LoginRequest(email, password));
        HttpRequest request = createReq(json, "/login");

        sendAsync(request,
                body -> {
                    try {
                        if (body.contains("token")) {
                            LoginResponse res =
                                    mapper.readValue(body, LoginResponse.class);
                            AuthState.setToken(res.token);
                            onSuccess.accept(res);
                        } else {
                            MessageResponse res = mapper.readValue(body, MessageResponse.class);
                            onSuccess.accept(res);
                        }
                    } catch (Exception e) {
                        onError.accept(e);
                    }
                },
                onError
        );
    }

    public static void getUser(
            Consumer<UserData> onSuccess,
            Consumer<Throwable> onError
    ) {
        if (!AuthState.isAuthenticated()) {
            onError.accept(new IllegalStateException("Not authenticated"));
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/me"))
                .header("Authorization", "Bearer " + AuthState.getToken())
                .GET()
                .build();

        sendAsync(request,
                body -> {
                    try {
                        UserData user =
                                mapper.readValue(body, UserData.class);
                        onSuccess.accept(user);
                    } catch (Exception e) {
                        onError.accept(e);
                    }
                },
                onError
        );
    }

    private static HttpRequest createReq(String json, String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    }

    private static String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    record LoginRequest(String email, String password) {}
    record RegisterRequest(String email, String username, String password) {}
}
