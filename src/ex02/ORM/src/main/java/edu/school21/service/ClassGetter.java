package edu.school21.service;

import java.io.File;

public class ClassGetter {
    private static final String pathBegin = "src.main.java.";

    public static Class[] getClasses(String packageName) throws ClassNotFoundException {
        String path = (pathBegin + packageName).replace('.', '/');
        File packageFolder = new File(path);
        File[] listFiles = packageFolder.listFiles();
        if (!packageFolder.isDirectory()) {
            System.err.println("Provided incorrect path");
            return null;
        }
        Class<?>[] classes = new Class[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            String className = listFiles[i].getName();
            className = className.substring(0, className.indexOf(".java"));
            classes[i] = Class.forName(packageName + "." + className);
        }
        return classes;
    }
}

