package com.cohesion;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println(
                "if you wanna test the scanner then uncomment the below lines and run mvn clean package and \r\n" + //
                        "java -cp target/Ser516Group4-1.0-SNAPSHOT.jar com.cohesion.Main <path to your java project>" + //
                        "replace java -cp target/classes com.cohesion.Main src/main/java");
        int M = 8;  // replace after Task 15- number of methods considered
        int F = 4;  // replace  after Task 16- number of instance fields
        double sumOfFieldToMethods = 24; // replace after Task 26- sum Of Field To Methods
        String metricName = "LCOMHS";

        double lcomhs = LcomhsCalculator.computeLcomhs(M, F, sumOfFieldToMethods);

        System.out.println("Metric: " + metricName);
        System.out.println("M=" + M + ", F=" + F + ", SumOfFieldToMethods=" + sumOfFieldToMethods);
        System.out.printf("LCOMHS=%.6f%n", lcomhs);
        // if (args.length == 0) {
        // System.out.println("Usage: java -cp <jar> com.cohesion.Main <path>");
        // return;
        // }

        // ProjectScanner scanner = new ProjectScanner();
        // List<Path> javaFiles = scanner.scanProject(args[0]);

        // System.out.println("Found " + javaFiles.size() + " Java files:");

        // javaFiles.forEach(System.out::println);
    }
}
