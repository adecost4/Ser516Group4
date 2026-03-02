package com.TaigaAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.*;
import java.time.Duration;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class TaigaApiClient {

    private final String base = System.getenv().getOrDefault(
            "TAIGA_BASE_URL",
            "https://swent0linux.asu.edu/taiga/api/v1"
    );

    private final boolean insecureSsl = "true".equalsIgnoreCase(
            System.getenv().getOrDefault("TAIGA_INSECURE_SSL", "false")
    );

    private final HttpClient client;
    private final ObjectMapper mapper;

    private String authToken;

    public TaigaApiClient() {
        this.client = buildHttpClient();
        this.mapper = new ObjectMapper();
        System.out.println("[TaigaApiClient] Using base URL: " + base);
        System.out.println("[TaigaApiClient] Insecure SSL: " + insecureSsl);
    }

    private HttpClient buildHttpClient() {
        HttpClient.Builder b = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL);

        if (!insecureSsl) {
            return b.build();
        }

        try {
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new SecureRandom());

            return b.sslContext(sc).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create insecure SSL HttpClient", e);
        }
    }

    public void login(String username, String password) {
        System.out.println("[login] Starting login...");

        String url = base + "/auth";

        String body = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8)
                + "&type=normal";

        System.out.println("[login] POST " + url);
        System.out.println("[login] (debug) About to send request...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("[login] (debug) Request returned.");
            System.out.println("[login] status=" + response.statusCode());

            if (response.statusCode() != 200) {
                System.out.println("[login] response body:\n" + response.body());
                throw new RuntimeException("Login failed: HTTP " + response.statusCode());
            }

            JsonNode json = mapper.readTree(response.body());
            JsonNode tokenNode = json.get("auth_token");
            if (tokenNode == null || tokenNode.isNull()) {
                System.out.println("[login] Missing auth_token in response:\n" + response.body());
                throw new RuntimeException("Login failed: auth_token missing");
            }

            this.authToken = tokenNode.asText();
            System.out.println("[login] Login successful! auth_token acquired.");

        } catch (HttpTimeoutException e) {
            System.out.println("[login] TIMEOUT: The request timed out (container may not reach the server).");
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (SSLHandshakeException e) {
            System.out.println("[login] SSL HANDSHAKE ERROR: certificate/trust issue with HTTPS.");
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (IOException | InterruptedException e) {
            System.out.println("[login] IO/INTERRUPT ERROR:");
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (RuntimeException e) {
            System.out.println("[login] RUNTIME ERROR:");
            e.printStackTrace();
            throw e;
        }
    }

    private HttpRequest buildGet(String url) {
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalStateException("Not logged in (authToken missing). Call login() first.");
        }

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();
    }

    private JsonNode sendGetJson(String url, String label) throws IOException, InterruptedException {
        System.out.println("[" + label + "] GET " + url);

        HttpResponse<String> response = client.send(buildGet(url), HttpResponse.BodyHandlers.ofString());
        System.out.println("[" + label + "] status=" + response.statusCode());

        if (response.statusCode() != 200) {
            System.out.println("[" + label + "] response body:\n" + response.body());
            throw new RuntimeException(label + " failed: HTTP " + response.statusCode());
        }

        return mapper.readTree(response.body());
    }

    public JsonNode getProjectBySlug(String slug) throws IOException, InterruptedException {
        String encoded = URLEncoder.encode(slug, StandardCharsets.UTF_8);
        return sendGetJson(base + "/projects/by_slug?slug=" + encoded, "getProjectBySlug");
    }

    public JsonNode getMilestones(long projectId) throws IOException, InterruptedException {
        return sendGetJson(base + "/milestones?project=" + projectId, "getMilestones");
    }

    public JsonNode getMilestone(long milestoneId) throws IOException, InterruptedException {
        return sendGetJson(base + "/milestones/" + milestoneId, "getMilestone");
    }
}