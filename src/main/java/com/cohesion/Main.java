package com.cohesion;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.cohesion.classes.MFResult;
import com.cohesion.classparsing.LCOMHSClassParser;

public class Main {

    public static void main(String[] args) {
        // System.out.println(
        //         "if you wanna test the scanner run mvn clean package and \r\n" + //
        //                 "java -cp target/Ser516Group4-1.0-SNAPSHOT.jar com.cohesion.Main <path to your java project>");

        if (args.length == 0) {
        System.out.println("Usage: java -cp <jar> com.cohesion.Main <path>");
        return;
        }

        ProjectScanner scanner = new ProjectScanner();
        List<Path> javaFiles = scanner.scanProject(args[0]);

        System.out.println("Found " + javaFiles.size() + " Java files:");

        javaFiles.forEach(System.out::println);
        LCOMHSClassParser parser = new LCOMHSClassParser();
        List<MetricRecord> records = new ArrayList<>();

        double lcomhs;
        for (Path file : javaFiles) {
            List<MFResult> results = parser.getMFForFile(file.toFile()); // Task 17
            for (MFResult r : results) {

            long timestamp=System.currentTimeMillis()/1000;
            // Compute LCOMHS
            lcomhs = LCOMHSCalculator.computeLcomhs(r.getM(), r.getF(), r.getSUMMF());   
            records.add(new MetricRecord(r.getPackageName(), r.getClassName(), "LCOMHS", lcomhs, timestamp));       
                System.out.println("File: " + file.getFileName());
                System.out.println("Class: " + r.getClassName());
                System.out.println("M (methods+ctors): " + r.getM());
                System.out.println("F (instance fields): " + r.getF());
                System.out.println("MF: " + r.getSUMMF());
                System.out.printf("LCOMHS=%.6f%n", lcomhs);
                System.out.println("----------------------------------");
            }
        }
        System.out.println("\n=== JSON Output ===");
        System.out.println(MetricFormatter.toJson(records));

        System.out.println("\n=== Prometheus Output ===");
        System.out.println(MetricFormatter.toPrometheus(records));
    }
}
