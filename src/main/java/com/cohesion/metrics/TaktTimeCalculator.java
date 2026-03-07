package com.cohesion.metrics;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class TaktTimeCalculator {

    public static class TaktPoint {
        private final LocalDate day;
        private final int delivered;
        private final double taktTime;

        public TaktPoint(LocalDate day, int delivered, double taktTime) {
            this.day = day;
            this.delivered = delivered;
            this.taktTime = taktTime;
        }

        public LocalDate getDay() {
            return day;
        }

        public int getDelivered() {
            return delivered;
        }

        public double getTaktTime() {
            return taktTime;
        }
    }

    public static List<TaktPoint> computeDaily(JsonNode milestone) {
        LocalDate start = LocalDate.parse(milestone.get("estimated_start").asText());
        LocalDate end = LocalDate.parse(milestone.get("estimated_finish").asText());

        Map<LocalDate, Integer> deliveredByDay = new HashMap<>();

        JsonNode stories = milestone.get("user_stories");
        if (stories != null && stories.isArray()) {
            for (JsonNode us : stories) {
                JsonNode finish = us.get("finish_date");
                if (finish != null && !finish.isNull()) {
                    String finishStr = finish.asText();

                    // format "YYYY-MM-DD" OR full ISO timestamp with Z
                    LocalDate d;
                    if (finishStr.length() >= 10 && finishStr.charAt(10) == 'T') {
                        d = java.time.OffsetDateTime.parse(finishStr).toLocalDate();
                    } else {
                        d = LocalDate.parse(finishStr);
                    }
                    deliveredByDay.put(d, deliveredByDay.getOrDefault(d, 0) + 1);
                }
            }
        }

        List<TaktPoint> points = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            if (!isWorkingDay(d))
                continue;

            int delivered = deliveredByDay.getOrDefault(d, 0);
            double takt = (delivered == 0) ? Double.NaN : (1.0 / delivered);

            points.add(new TaktPoint(d, delivered, takt));
        }

        return points;
    }

    private static boolean isWorkingDay(LocalDate d) {
        DayOfWeek w = d.getDayOfWeek();
        return w != DayOfWeek.SATURDAY && w != DayOfWeek.SUNDAY;
    }
}