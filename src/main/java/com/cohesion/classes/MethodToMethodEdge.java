package com.cohesion.classes;

/**
 * Represents an edge from one method to another method in the same class.
 * Used for building method call graphs (Task #20).
 */
public class MethodToMethodEdge {
    private final String className;
    private final String callerMethod;
    private final String calledMethod;

    public MethodToMethodEdge(String className, String callerMethod, String calledMethod) {
        this.className = className;
        this.callerMethod = callerMethod;
        this.calledMethod = calledMethod;
    }

    public String getClassName() {
        return className;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public String getCalledMethod() {
        return calledMethod;
    }

    @Override
    public String toString() {
        return className + "." + callerMethod + " -> " + className + "." + calledMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MethodToMethodEdge edge = (MethodToMethodEdge) obj;
        return className.equals(edge.className) &&
               callerMethod.equals(edge.callerMethod) &&
               calledMethod.equals(edge.calledMethod);
    }

    @Override
    public int hashCode() {
        return className.hashCode() * 31 + callerMethod.hashCode() * 17 + calledMethod.hashCode();
    }
}
