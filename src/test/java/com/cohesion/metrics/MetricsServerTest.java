package com.cohesion.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetricsServerTest {

    private MetricsServer server;
    private int port;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() throws Exception {
        port = getFreePort();
        server = new MetricsServer();
        server.start(port);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void lcomhsEndpointReturnsPrometheusMetric() throws Exception {
        MetricsServer.LCOMHS_GAUGE.labels("TestClass", "com.test").set(0.5);

        HttpResponse<String> response = get("/metrics/lcomhs");

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("")
                .startsWith("text/plain; version=0.0.4"));
        assertTrue(response.body().contains("lcomhs"));
        assertTrue(response.body().contains("class=\"TestClass\""));
        assertTrue(response.body().contains("package=\"com.test\""));
    }

    @Test
    void taktTimeEndpointReturnsPrometheusMetric() throws Exception {
        MetricsServer.TAKT_TIME_GAUGE.labels("Sprint 1", "2024-10-01").set(1.0);

        HttpResponse<String> response = get("/metrics/takt-time");

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("")
                .startsWith("text/plain; version=0.0.4"));
        assertTrue(response.body().contains("takt_time_days_per_story"));
        assertTrue(response.body().contains("sprint=\"Sprint 1\""));
        assertTrue(response.body().contains("date=\"2024-10-01\""));
    }

    @Test
    void leadTimeEndpointReturnsPrometheusMetric() throws Exception {
        MetricsServer.LEAD_TIME_GAUGE.labels("Story A").set(3);

        HttpResponse<String> response = get("/metrics/leadtime");

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("")
                .startsWith("text/plain; version=0.0.4"));
        assertTrue(response.body().contains("lead_time"));
        assertTrue(response.body().contains("name=\"Story A\""));
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private int getFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
