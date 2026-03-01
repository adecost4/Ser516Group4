package com.cohesion.metrics;

import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class MetricsServer {

    private static final CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    public static final Gauge LCOMHS_GAUGE = Gauge.build()
            .name("lcomhs")
            .help("LCOMHS cohesion metric per Java class")
            .labelNames("class", "package")
            .register(registry);

    private HttpServer server;

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/metrics/lcomhs", exchange -> {
            exchange.getResponseHeaders().set("Content-Type", TextFormat.CONTENT_TYPE_004);
            exchange.sendResponseHeaders(200, 0);

            try (Writer writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8)) {
                TextFormat.write004(writer,
                        registry.filteredMetricFamilySamples(Collections.singleton("lcomhs")));
            }

            exchange.close();
        });

        server.start();
        System.out.println("LCOMHS Metrics running at http://localhost:" + port + "/metrics/lcomhs");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}