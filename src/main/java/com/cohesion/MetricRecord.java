package com.cohesion;

public class MetricRecord {

    private final String packageName;
    private final String className;
    private final String metric;
    private final double value;
    private final long timestamp;

    public MetricRecord(String packageName, String className, String metric,
                        double value, long timestamp) {
        this.packageName = packageName;
        this.className = className;
        this.metric = metric;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
