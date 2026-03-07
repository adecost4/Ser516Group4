package com.cohesion;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.TaigaAPI.TaigaApiClient;
import com.cohesion.classparsing.LCOMHSClassParser;
import com.cohesion.classes.MFResult;
import com.cohesion.metrics.MetricsServer;
import com.cohesion.metrics.TaktTimeCalculator;
import com.leadtime.LeadTimeRetriever;
import com.fasterxml.jackson.databind.JsonNode;

public class Main {

    private static String env(String key) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    public static void main(String[] args) {
        try {
            MetricsServer metricsServer = new MetricsServer();
            metricsServer.start(8080);
            System.out.println("[Main] Metrics server started on :8080");

            if (args != null && args.length > 0 && args[0] != null && !args[0].isBlank()) {
                String projectPath = args[0].trim();
                System.out.println("[Main] Running LCOMHS scan on path: " + projectPath);

                ProjectScanner scanner = new ProjectScanner();
                List<Path> javaFiles = scanner.scanProject(projectPath);

                LCOMHSClassParser parser = new LCOMHSClassParser();

                for (Path file : javaFiles) {
                    List<MFResult> results = parser.getMFForFile(file.toFile());
                    for (MFResult r : results) {
                        double lcomhs = LCOMHSCalculator.computeLcomhs(r.getM(), r.getF(), r.getSUMMF());
                        MetricsServer.LCOMHS_GAUGE.labels(r.getClassName(), r.getPackageName()).set(lcomhs);
                    }
                }

                System.out.println("[Main] LCOMHS scan complete. Metrics updated.");
            } else {
                System.out.println("[Main] No <path> arg provided. Skipping LCOMHS scan.");
            }

            // ----------------------------
            // Taiga retrieval
            // ----------------------------
            String username = env("TAIGA_USERNAME");
            String password = env("TAIGA_PASSWORD");
            String slug = env("TAIGA_PROJECT_SLUG");
            String sprintIndexStr = env("TAIGA_SPRINT_INDEX");
            int sprintIndex = (sprintIndexStr == null) ? 0 : Integer.parseInt(sprintIndexStr);

            if (username == null || password == null || slug == null) {
                System.out.println("[Main] TAIGA configuration missing. Skipping Taiga.");
            } else {

                TaigaApiClient taiga = new TaigaApiClient();
                taiga.login(username, password);

                JsonNode project = taiga.getProjectBySlug(slug);
                long projectId = project.get("id").asLong();

                JsonNode sprints = taiga.getMilestones(projectId);

                if (sprintIndex < 0 || sprintIndex >= sprints.size()) {
                    throw new RuntimeException("TAIGA_SPRINT_INDEX out of range: " + sprintIndex);
                }

                long milestoneId = sprints.get(sprintIndex).get("id").asLong();
                JsonNode milestone = taiga.getMilestone(milestoneId);

                String sprintName = milestone.get("name").asText();
                var points = TaktTimeCalculator.computeDaily(milestone);
                int publishedCount = 0;
                for (TaktTimeCalculator.TaktPoint p : points) {
                    if (p.getDelivered() > 0) {

                        double takt = p.getTaktTime();

                        MetricsServer.TAKT_TIME_GAUGE
                                .labels(sprintName, p.getDay().toString())
                                .set(takt);


                        publishedCount++;
                    }
                }

                List<Integer> closedUserStoryIds = taiga.getClosedUserStoryIds();
                Map<String, Integer> leadTimes = LeadTimeRetriever.getLeadTimeInfo(taiga, closedUserStoryIds); // TODO: Expose  in Task 50
                leadTimes.forEach((name, time) -> {
                    MetricsServer.LEAD_TIME_GAUGE.labels(name).set(time);
                });
            }
            System.out.println("[Main] Running. Metrics available on :8080. Ctrl+C to stop.");
            Thread.currentThread().join();

        } catch (Exception e) {
            System.out.println("[Main] FATAL ERROR:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}