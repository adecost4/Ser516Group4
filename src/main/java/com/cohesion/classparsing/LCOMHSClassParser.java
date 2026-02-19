package com.cohesion.classparsing;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.cohesion.classes.MFResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;


public class LCOMHSClassParser {
    // count class methods and constructors
    public int countClassMethods(ClassOrInterfaceDeclaration cls) {
        return cls.getMethods().size() + cls.getConstructors().size();
    }

    // count class instance fields
    public int countCountClassInstanceFields(ClassOrInterfaceDeclaration cls) {
        return (int) cls.getFields().stream()
                .filter(field -> !field.isStatic())
                .count();
    }

    // count how many times each instance field is accessed in the class
    public int countSumOfMethodFieldAccesses(ClassOrInterfaceDeclaration cls) {
        // Get all instance field names
        List<VariableDeclarator> fields = cls.getFields().stream()
                .filter(f -> !f.isStatic()) // static fields are not included
                .flatMap(f -> f.getVariables().stream()) // account for declarations like "private int x, y;"
                .collect(Collectors.toList());;
        
        // Get all non-static methods
        List<MethodDeclaration> methods = cls.getMethods().stream()
                .filter(m -> !m.isStatic()) // static methods can't access instance fields
                .collect(Collectors.toList());

        int sum = 0;

        for (VariableDeclarator field : fields) {
            String fieldName = field.getNameAsString();
            for (MethodDeclaration method : methods) {
                int calc = 1 + 1;
            }
        }

        for (MethodDeclaration method : methods) {
            List<NameExpr> nameExprs = method.findAll(NameExpr.class); // variable access
            List<FieldAccessExpr> fieldAccessExprs = method.findAll(FieldAccessExpr.class); // this.variable access

            for (NameExpr nameExpr : nameExprs) {
                if (fields.stream().anyMatch(f -> f.getNameAsString().equals(nameExpr.getNameAsString()))) {
                    sum++;
                }
            }

            for (FieldAccessExpr fieldAccessExpr : fieldAccessExprs) {
                if (fields.stream().anyMatch(f -> f.getNameAsString().equals(fieldAccessExpr.getNameAsString()))) {
                    sum++;
                }
            }
        }

        return sum;
    }

    //task 17
    public List<MFResult> getMFForFile(File file) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);

            return cu.findAll(ClassOrInterfaceDeclaration.class)
                    .stream()
                    .filter(cls -> !cls.isInterface()) 
                    .map(cls -> {
                        int m = countClassMethods(cls); // Task 15
                        int f = countCountClassInstanceFields(cls); // Task 16
                        int sumMF = countSumOfMethodFieldAccesses(cls); // Task 26
                        return new MFResult(cls.getNameAsString(), m, f, sumMF);
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing file: " + file, e);
        }
    }
}
