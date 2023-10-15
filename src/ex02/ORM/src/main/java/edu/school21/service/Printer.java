package edu.school21.service;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Printer {

    private static final String packageName = "edu.school21.model";
    public static void printClasses() {
        System.out.println("Classes:");
        try {
            Class<?>[] classes =  ClassGetter.getClasses(packageName);
            for (Class<?> classInPackage : classes){
                System.out.println("\t- " + ObjectInfo.getClassName(classInPackage.getName()));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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


    }



