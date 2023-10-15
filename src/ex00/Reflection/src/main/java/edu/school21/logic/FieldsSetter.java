package edu.school21.logic;

import java.lang.reflect.Field;
import java.util.Scanner;

public class FieldsSetter {

    public static void setObjectFields(Field[] fields, Scanner scanner, Object object) throws IllegalAccessException {
        System.out.println("Let’s create an object.");
        for (Field field : fields) {
            System.out.println(field.getName() + ":");
            setField(field, false, scanner, object);
        }
    }

    public static void updateObject(Field[] fields, Scanner scanner, Object object) throws IllegalAccessException {
        System.out.println("---------------------\n" +
                "Enter name of the field for changing:");
        String fieldName = scanner.nextLine();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                setField(field, true, scanner, object);
            }
        }
    }

    private static void setField(Field field, boolean isUpdateNeeded, Scanner scanner, Object object) throws IllegalAccessException {
        String type = ObjectInfo.getFieldType(field);
        field.setAccessible(true);
        switch (type) {
            case ("String"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextLine());
                break;
            }
            case ("Integer"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextInt());
                scanner.nextLine();
                break;
            }
            case ("Double"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextDouble());
                scanner.nextLine();
                break;
            }
            case ("Boolean"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextBoolean());
                scanner.nextLine();
                break;
            }
            case ("Long"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextLong());
                scanner.nextLine();
                break;
            }
        }
    }
    private static void printTypeForUpdate(boolean isUpdateNeeded, String type) {
        if (isUpdateNeeded) {
            System.out.println("Enter " + type + " value:");
        }
    }
}
