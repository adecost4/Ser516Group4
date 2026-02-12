package com.cohesion;

import java.nio.file.Path;
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
    }
}
