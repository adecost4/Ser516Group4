package com.cohesion.classparsing;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cohesion.classes.MFResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
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
        List<CallableDeclaration<?>> methods = cls.getMethods().stream()
                .filter(m -> !m.isStatic()) // static methods can't access instance fields
                .collect(Collectors.toList());

        // Get all constructors
        List<CallableDeclaration<?>> constructors = cls.getConstructors().stream()
                .collect(Collectors.toList());

        // Combine methods and constructors
        List<CallableDeclaration<?>> methodsAndConstructors = Stream.concat(methods.stream(), constructors.stream()).collect(Collectors.toList());

        int sum = 0;

        for (VariableDeclarator field : fields) {
            String fieldName = field.getNameAsString();
            for (CallableDeclaration<?> m : methodsAndConstructors) {
                boolean fieldAccessed = false;

                // check for simple access
                List<NameExpr> simpleFieldAccesses = m.findAll(NameExpr.class, f -> f.getNameAsString().equals(fieldName));
                if (simpleFieldAccesses.size() > 0) {
                    for (NameExpr var : simpleFieldAccesses) {
                        // make sure the variable is actually the field and not a local variable with the same name
                        ResolvedValueDeclaration resolvedVar = var.resolve();
                        if (resolvedVar.isField()) {
                            fieldAccessed = true;
                            break;
                        }
                    }
                }
                // check for field access like "this.fieldName"
                else {
                    fieldAccessed = m.findAll(FieldAccessExpr.class, f -> f.getNameAsString().equals(fieldName)).size() > 0;
                }
                
                if (fieldAccessed) {
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
