package com.cohesion;

import java.util.List;
import java.util.Locale;

public class MetricFormatter {

    private MetricFormatter() {
    }

    public static String toJson(List<MetricRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"results\":[");
        for (int i = 0; i < records.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            MetricRecord r = records.get(i);
            sb.append("{");
            sb.append("\"package\":\"").append(escapeJson(r.getPackageName())).append("\",");
            sb.append("\"class\":\"").append(escapeJson(r.getClassName())).append("\",");
            sb.append("\"metric\":\"").append(escapeJson(r.getMetric())).append("\",");
            sb.append(String.format(Locale.US, "\"value\":%f,", r.getValue()));
            sb.append("\"timestamp\":").append(r.getTimestamp());
            sb.append("}");
        }
        sb.append("]}");
        return sb.toString();
    }

    public static String toPrometheus(List<MetricRecord> records) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            MetricRecord r = records.get(i);
            String metricName = r.getMetric().toLowerCase(Locale.ROOT);
            sb.append(metricName);
            sb.append("{class=\"").append(r.getClassName()).append("\",");
            sb.append("package=\"").append(r.getPackageName()).append("\",");
            sb.append("metric=\"").append(r.getMetric()).append("\"}");
            sb.append(String.format(Locale.US, " %f %d", r.getValue(), r.getTimestamp()));
            if (i < records.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
