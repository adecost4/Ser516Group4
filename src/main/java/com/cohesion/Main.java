package com.cohesion;

import java.nio.file.Path;
import java.util.List;

import com.TaigaAPI.TaigaApiClient;
import com.cohesion.classparsing.LCOMHSClassParser;
import com.cohesion.classes.MFResult;
import com.cohesion.metrics.MetricsServer;
import com.cohesion.metrics.TaktTimeCalculator;
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
System.out.println("[Main] TAIGA env vars not fully set. Skipping Taiga.");
System.out.println(
"[Main] Need TAIGA_USERNAME, TAIGA_PASSWORD, TAIGA_PROJECT_SLUG (optional TAIGA_SPRINT_INDEX, TAIGA_BASE_URL).");
} else {
System.out.println("[Main] Starting Taiga retrieval...");
System.out.println("[Main] username=" + username);
System.out.println("[Main] slug=" + slug);
System.out.println("[Main] sprintIndex=" + sprintIndex);

TaigaApiClient taiga = new TaigaApiClient();
taiga.login(username, password);

System.out.println("[Main] Fetching project...");
JsonNode project = taiga.getProjectBySlug(slug);

long projectId = project.get("id").asLong();
System.out.println("[Main] Project found! id=" + projectId + " name=" + project.get("name").asText());

System.out.println("[Main] Fetching milestones...");
JsonNode sprints = taiga.getMilestones(projectId);

System.out.println("[Main] Total sprints returned: " + sprints.size());
for (int i = 0; i < sprints.size(); i++) {
JsonNode s = sprints.get(i);
System.out.println(" " + i + ": " + s.get("name").asText()
+ " (" + s.get("estimated_start").asText()
+ " -> " + s.get("estimated_finish").asText() + ")");
}

if (sprintIndex < 0 || sprintIndex >= sprints.size()) {
throw new RuntimeException("TAIGA_SPRINT_INDEX out of range: " + sprintIndex);
}

long milestoneId = sprints.get(sprintIndex).get("id").asLong();
System.out.println("[Main] Fetching chosen milestone id=" + milestoneId + "...");
JsonNode milestone = taiga.getMilestone(milestoneId);

System.out.println("[Main] Chosen sprint: " + milestone.get("name").asText());
System.out.println("[Main] Taiga retrieval done.");
String sprintName = milestone.get("name").asText();
var points = TaktTimeCalculator.computeDaily(milestone);

System.out.println("[Main] Takt Time points (working days): " + points.size());

for (TaktTimeCalculator.TaktPoint p : points) {
String day = p.getDay().toString();
MetricsServer.TAKT_TIME_GAUGE.labels(sprintName, day).set(p.getTaktTime());
System.out.println(" date=" + day + " delivered=" + p.getDelivered() + " takt=" + p.getTaktTime());
}

System.out.println("[Main] Published takt_time_days_per_story to Prometheus.");
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