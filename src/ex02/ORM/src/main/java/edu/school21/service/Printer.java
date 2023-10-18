package edu.school21.service;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Printer {


    public static void printClasses(Class<?>[] classes) {
        System.out.println("Classes:");
        for (Class<?> classInPackage : classes){
            System.out.println("\t- " + ObjectInfo.getClassName(classInPackage.getName()));
        }
        System.out.println("---------------------\nEnter class name:");
    }


    public static void printFields(Field[] fields) {
        System.out.println("---------------------");
        System.out.println("fields:");
        for (Field field : fields) {
            System.out.println("\t\t" + ObjectInfo.getFieldType(field) + " " + field.getName());
        }
    }


    public static void printQuery(String query) {
        System.out.println("Generated SQL: " + query);
    }
}



