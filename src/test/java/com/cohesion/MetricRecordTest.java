package com.cohesion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricRecordTest {

    @Test
    void constructor_andGetters_returnExpectedValues() {
        MetricRecord record = new MetricRecord(
                "com.cohesion",
                "Main",
                "lcomhs",
                0.75,
                1710000000L
        );

        assertEquals("com.cohesion", record.getPackageName());
        assertEquals("Main", record.getClassName());
        assertEquals("lcomhs", record.getMetric());
        assertEquals(0.75, record.getValue(), 0.000001);
        assertEquals(1710000000L, record.getTimestamp());
    }

    @Test
    void supportsEmptyStrings_andZeroTimestamp() {
        MetricRecord record = new MetricRecord(
                "",
                "",
                "",
                0.0,
                0L
        );

        assertEquals("", record.getPackageName());
        assertEquals("", record.getClassName());
        assertEquals("", record.getMetric());
        assertEquals(0.0, record.getValue(), 0.000001);
        assertEquals(0L, record.getTimestamp());
    }

    @Test
    void supportsNegativeMetricValues_andNegativeTimestamp() {
        MetricRecord record = new MetricRecord(
                "pkg",
                "Cls",
                "lead_time",
                -5.5,
                -1L
        );

        assertEquals("pkg", record.getPackageName());
        assertEquals("Cls", record.getClassName());
        assertEquals("lead_time", record.getMetric());
        assertEquals(-5.5, record.getValue(), 0.000001);
        assertEquals(-1L, record.getTimestamp());
    }
}
