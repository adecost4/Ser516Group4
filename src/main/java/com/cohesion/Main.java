package com.cohesion;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println(
                "if you wanna test the scanner then uncomment the below lines and run mvn clean package and \r\n" + //
                        "java -cp target/Ser516Group4-1.0-SNAPSHOT.jar com.cohesion.Main <path to your java project>");

        // if (args.length == 0) {
        // System.out.println("Usage: java -cp <jar> com.cohesion.Main <path>");
        // return;
        // }

        // ProjectScanner scanner = new ProjectScanner();
        // List<Path> javaFiles = scanner.scanProject(args[0]);

        // System.out.println("Found " + javaFiles.size() + " Java files:");

        // javaFiles.forEach(System.out::println);

        List<MetricRecord> records = new ArrayList<>();
        records.add(new MetricRecord("com.sample.football", "FootballTeam", "LCOMHS", 0.214286, 1738890000L));
        records.add(new MetricRecord("com.sample.football", "FootballTeam", "LCOM4", 3.0, 1738890000L));
        records.add(new MetricRecord("com.sample.football", "Player", "LCOMHS", 0.500000, 1738890000L));
        records.add(new MetricRecord("com.sample.football", "Player", "LCOM4", 2.0, 1738890000L));

        System.out.println("\n=== JSON Output ===");
        System.out.println(MetricFormatter.toJson(records));

        System.out.println("\n=== Prometheus Output ===");
        System.out.println(MetricFormatter.toPrometheus(records));
    }
}
