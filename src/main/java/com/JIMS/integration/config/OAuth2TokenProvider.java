package com.JIMS.integration.config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class OAuth2TokenProvider {

    public static String getAccessToken(String clientId, String clientSecret, String tenantId) throws IOException, InterruptedException {
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        // Prepare the request body with required parameters
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "client_credentials");
        params.put("scope", "https://graph.microsoft.com/.default");

        String formBody = getFormBody(params);

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUrl))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formBody))
            .build();

        // Send the HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Extract the access token from the response
            String responseBody = response.body();
            return extractAccessToken(responseBody);
        } else {
            throw new RuntimeException("Failed to fetch access token. Response: " + response.body());
        }
    }

    private static String getFormBody(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                       URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return joiner.toString();
    }

    private static String extractAccessToken(String responseBody) {
        // Assuming response is JSON. Use a JSON library like Jackson or Gson to parse it.
        // Example response: {"access_token":"<token>","token_type":"Bearer","expires_in":3600}
        String accessToken = responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        return accessToken;
    }
}

