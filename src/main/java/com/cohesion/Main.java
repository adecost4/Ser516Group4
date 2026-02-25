package com.cohesion;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.cohesion.classes.MFResult;
import com.cohesion.classparsing.LCOMHSClassParser;
import com.cohesion.metrics.MetricsServer;

public class Main {

    public static void main(String[] args) throws Exception{
        MetricsServer metricsServer = new MetricsServer();
        metricsServer.start(8080);
        // System.out.println(
        //         "if you wanna test the scanner run mvn clean package and \r\n" + //
        //                 "java -cp target/Ser516Group4-1.0-SNAPSHOT.jar com.cohesion.Main <path to your java project>");

        if (args.length == 0) {
        System.out.println("Usage: java -cp <jar> com.cohesion.Main <path>");
        return;
        }

        ProjectScanner scanner = new ProjectScanner();
        List<Path> javaFiles = scanner.scanProject(args[0]);
        LCOMHSClassParser parser = new LCOMHSClassParser();
        List<MetricRecord> records = new ArrayList<>();

        double lcomhs;
        for (Path file : javaFiles) {
            List<MFResult> results = parser.getMFForFile(file.toFile()); // Task 17
            for (MFResult r : results) {

            long timestamp=System.currentTimeMillis()/1000;
            // Compute LCOMHS
            lcomhs = LCOMHSCalculator.computeLcomhs(r.getM(), r.getF(), r.getSUMMF());
            MetricsServer.LCOMHS_GAUGE
            .labels(r.getClassName(), r.getPackageName())
            .set(lcomhs);
            }
        }
    }
}
