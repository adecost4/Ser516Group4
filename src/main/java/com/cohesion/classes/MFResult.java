package com.cohesion.classes;

public class MFResult {
    private final String className;
    private final int M;
    private final int F;

    public MFResult(String className, int m, int f) {
        this.className = className;
        this.M = m;
        this.F = f;
    }

    public String getClassName() {
        return className;
    }

    public int getM() {
        return M;
    }

    public int getF() {
        return F;
    }
}