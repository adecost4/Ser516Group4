package com.cohesion.classparsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Task #20: Create tree - create edges from in-class methods to other in class methods when called.
 */
public class MethodCallTreeBuilder {

    /**
     * Extracts method-to-method call graph for all classes in a file.
     * Returns a map where key = caller method, value = list of methods it calls.
     */
    public Map<String, List<String>> getMethodToMethodEdges(File file) {
        try {
            String content = Files.readString(file.toPath());
            return getMethodToMethodEdgesForClass(content);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file, e);
        }
    }

    /**
     * Extracts method-to-method call graph from class source code.
     * Returns a map where key = caller method, value = list of methods it calls.
     */
    public Map<String, List<String>> getMethodToMethodEdgesForClass(String content) {
        Map<String, List<String>> tree = new HashMap<>();
        
        // Get class name
        Matcher classMatcher = Pattern.compile("\\bclass\\s+(\\w+)").matcher(content);
        if (!classMatcher.find()) return tree;
        String className = classMatcher.group(1);

        // Find all method names (including constructor)
        Set<String> methods = new HashSet<>();
        Matcher m = Pattern.compile("\\b(\\w+)\\s*\\([^)]*\\)\\s*\\{").matcher(content);
        while (m.find()) methods.add(m.group(1));
        methods.add(className); // constructor

        // For each method, find what it calls
        m = Pattern.compile("\\b(\\w+)\\s*\\([^)]*\\)\\s*\\{").matcher(content);
        while (m.find()) {
            String caller = m.group(1);
            String body = extractBody(content, m.end() - 1);
            if (body != null) {
                List<String> called = new ArrayList<>();
                for (String method : methods) {
                    if (!method.equals(caller) && Pattern.compile("\\b" + Pattern.quote(method) + "\\s*\\(").matcher(body).find()) {
                        called.add(method);
                    }
                }
                if (!called.isEmpty()) {
                    tree.put(caller, called);
                }
            }
        }
        return tree;
    }

    private String extractBody(String content, int start) {
        int count = 0;
        for (int i = start; i < content.length(); i++) {
            if (content.charAt(i) == '{') count++;
            else if (content.charAt(i) == '}') {
                if (--count == 0) return content.substring(start + 1, i);
            }
        }
        return null;
    }
}
