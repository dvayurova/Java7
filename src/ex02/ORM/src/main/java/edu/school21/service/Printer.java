package edu.school21.service;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Printer {

    public static void printClasses(Class<?>[] classes) {
        System.out.println("Classes:");
        for (Class<?> classInPackage : classes){
            System.out.println("\t- " + getClassName(classInPackage.getName()));
        }
        System.out.println("---------------------\nEnter class name:");
    }

    public static void printQuery(String query) {
        System.out.println("Generated SQL: " + query);
    }

    private static String getClassName(String className) {
        return className.substring(className.indexOf("model.") + 6);
    }
}



