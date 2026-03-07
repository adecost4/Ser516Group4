package com.cohesion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetricFormatterTest {

    @Test
    void toJson_emptyList_returnsEmptyResultsArray() {
        String json = MetricFormatter.toJson(List.of());
        assertEquals("{\"results\":[]}", json);
    }

    @Test
    void toJson_singleRecord_formatsCorrectly() {
        MetricRecord r = new MetricRecord(
                "com.example",
                "MyClass",
                "lcomhs",
                0.5,
                123L
        );

        String json = MetricFormatter.toJson(List.of(r));

        assertTrue(json.startsWith("{\"results\":[{"));
        assertTrue(json.contains("\"package\":\"com.example\""));
        assertTrue(json.contains("\"class\":\"MyClass\""));
        assertTrue(json.contains("\"metric\":\"lcomhs\""));
        assertTrue(json.contains("\"value\":0.500000"));
        assertTrue(json.contains("\"timestamp\":123"));
        assertTrue(json.endsWith("}]}"));
    }

    @Test
    void toJson_multipleRecords_includesCommaBetweenObjects() {
        MetricRecord r1 = new MetricRecord("p1", "C1", "m1", 1.0, 1L);
        MetricRecord r2 = new MetricRecord("p2", "C2", "m2", 2.0, 2L);

        String json = MetricFormatter.toJson(List.of(r1, r2));
        
        assertTrue(json.contains("},{"), "Expected comma between objects in JSON array");
    }

    @Test
    void toJson_escapesQuotesAndBackslashes() {
        MetricRecord r = new MetricRecord(
                "com\\pkg\"name",
                "Cls\"A\\B",
                "met\"ric\\x",
                1.25,
                42L
        );

        String json = MetricFormatter.toJson(List.of(r));

        assertTrue(json.contains("\"package\":\"com\\\\pkg\\\"name\""));
        assertTrue(json.contains("\"class\":\"Cls\\\"A\\\\B\""));
        assertTrue(json.contains("\"metric\":\"met\\\"ric\\\\x\""));
    }

    @Test
    void toJson_nullStrings_areConvertedToEmptyString() {
        MetricRecord r = new MetricRecord(
                null,
                null,
                null,
                3.14,
                99L
        );

        String json = MetricFormatter.toJson(List.of(r));

        assertTrue(json.contains("\"package\":\"\""));
        assertTrue(json.contains("\"class\":\"\""));
        assertTrue(json.contains("\"metric\":\"\""));
    }

    @Test
    void toPrometheus_singleRecord_formatsCorrectly() {
        MetricRecord r = new MetricRecord(
                "com.example",
                "MyClass",
                "LCOMHS",
                0.75,
                1000L
        );

        String out = MetricFormatter.toPrometheus(List.of(r));

        assertTrue(out.startsWith("lcomhs{"));

        assertTrue(out.contains("class=\"MyClass\""));
        assertTrue(out.contains("package=\"com.example\""));
        assertTrue(out.contains("metric=\"LCOMHS\""));

        assertTrue(out.endsWith(" 0.750000 1000"));
    }

    @Test
    void toPrometheus_multipleRecords_separatedByNewline_noTrailingNewline() {
        MetricRecord r1 = new MetricRecord("p1", "C1", "M1", 1.0, 1L);
        MetricRecord r2 = new MetricRecord("p2", "C2", "M2", 2.0, 2L);

        String out = MetricFormatter.toPrometheus(List.of(r1, r2));

        assertTrue(out.contains("\n"), "Expected newline between records");
        assertFalse(out.endsWith("\n"), "Should not end with a trailing newline");
    }
}
