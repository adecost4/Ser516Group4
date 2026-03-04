package com.leadtime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import javax.net.ssl.SSLSession;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class LeadTimeCalculatorTest {
    
    private static class StubResponse implements HttpResponse<String> {
        private final String body;

        StubResponse(String body) {
            this.body = body;
        }

        @Override
        public int statusCode() {
            return 200;
        }

        @Override
        public HttpRequest request() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (k, v) -> true);
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1;
        }
    }

    @Test
    public void validClosedStoryReturnsDays() {
        String json = "{\"created_date\":\"2024-01-01T00:00:00Z\"," +
                      "\"finish_date\":\"2024-01-11T00:00:00Z\"," +
                      "\"is_closed\":true}";
        int leadTime = LeadTimeCalculator.calculateLeadTime(new StubResponse(json));
        assertEquals(10, leadTime, "Should compute ten days between the two timestamps");
    }

    @Test
    public void openStoryReturnsMinusOne() {
        String json = "{\"created_date\":\"2024-01-01T00:00:00Z\"," +
                      "\"finish_date\":\"2024-01-11T00:00:00Z\"," +
                      "\"is_closed\":false}";
        int leadTime = LeadTimeCalculator.calculateLeadTime(new StubResponse(json));
        assertEquals(-1, leadTime, "Open stories should return -1");
    }

    @Test
    public void invalidJsonReturnsMinusOne() {
        int leadTime = LeadTimeCalculator.calculateLeadTime(new StubResponse("not a json"));
        assertEquals(-1, leadTime, "Malformed JSON should be handled gracefully and return -1");
    }

    @Test
    public void sameDayClosedReturnsZero() {
        String json = "{\"created_date\":\"2024-02-14T08:00:00Z\"," +
                      "\"finish_date\":\"2024-02-14T20:00:00Z\"," +
                      "\"is_closed\":true}";
        int leadTime = LeadTimeCalculator.calculateLeadTime(new StubResponse(json));
        assertEquals(0, leadTime, "Stories finished the same day should yield 0 lead time");
    }
}

