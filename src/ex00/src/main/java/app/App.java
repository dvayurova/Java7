package app;

import classes.Car;
import classes.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Scanner;

public class App {

    private static Object object;
    private static Field[] fields;
    private static Method[] methods;
    private static Scanner scanner;

    public static void main(String[] args) {
        printClasses();
        scanner = new Scanner(System.in);
        String className = scanner.nextLine();
        try {
            object = createObject(className);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
            return;
        }
        printFields();
        printMethods();
        try {
            createObject();
            System.out.print("Object created: " + objectInfo());
            updateObject();
            System.out.print("Object updated: " + objectInfo());
        } catch (IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
        }
    }


    private static void printClasses() {
        System.out.println("Classes:");
        String userClass = User.class.getName();
        String carClass = Car.class.getName();
        System.out.println(" - " + getClassName(userClass));
        System.out.println(" - " + getClassName(carClass));
        System.out.println("---------------------\nEnter class name:");
    }

    private static String getClassName(String className) {
        return className.substring(className.indexOf(".") + 1);
    }

    private static Object createObject(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> selectedClass = Class.forName("classes." + className);
        return selectedClass.getDeclaredConstructor().newInstance();
    }

    private static void printFields() {
        System.out.println("---------------------");
        fields = object.getClass().getDeclaredFields();
        System.out.println("fields:");
        for (Field field : fields) {
            System.out.println("\t\t" + getFieldType(field) + " " + field.getName());
        }
    }

    private static String getFieldType(Field field) {
        String type = field.getType().getName();
        return type.contains("lang.") ? type.substring(type.indexOf("lang.") + 5) : type;
    }

    private static void printMethods() {
        methods = object.getClass().getMethods();
        System.out.println("methods:");
        for (Method method : methods) {
            String methodName = method.getName();
            if (isInheritedObjectMethods(methodName)) continue;
            String type = method.getReturnType().getName();
            type = type.contains("lang.") ? type.substring(type.indexOf("lang.") + 5) : type;
            System.out.println("\t\t" + type + " " + methodName + getMethodParameters(method.getParameters(), type));
        }
        System.out.println("---------------------");
    }

    private static String getMethodParameters(Parameter[] parameters, String type) {
        StringBuilder params = new StringBuilder("(");
        for (Parameter parameter : parameters) {
            String parameterType = parameter.getName();
            parameterType = type.contains("lang.") ? parameterType.substring(type.indexOf("lang.") + 5) : type;
            params.append(parameterType + ", ");
        }
        if (params.length() >= 2) {
            params.delete(params.length() - 2, params.length());
        }
        params.append(")");
        return params.toString();
    }

    private static boolean isInheritedObjectMethods(String methodName) {
        return (methodName.equals("toString") || methodName.equals("wait") || methodName.equals("equals") || methodName.equals("hashCode") || methodName.equals("getClass") || methodName.equals("notify") || methodName.equals("notifyAll"));
    }

    private static void createObject() throws IllegalAccessException {
        System.out.println("Let’s create an object.");
        for (Field field : fields) {
            System.out.println(field.getName() + ":");
            setField(field, false);
        }
    }

    private static void printTypeForUpdate(boolean isUpdateNeeded, String type) {
        if (isUpdateNeeded) {
            System.out.println("Enter " + type + " value:");
        }
    }

    private static void setField(Field field, boolean isUpdateNeeded) throws IllegalAccessException {
        String type = getFieldType(field);
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
                break;
            }
            case ("Boolean"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextBoolean());
                break;
            }
            case ("Long"): {
                printTypeForUpdate(isUpdateNeeded, type);
                field.set(object, scanner.nextLong());
                break;
            }
        }
    }

        private static String objectInfo () throws IllegalAccessException {
            StringBuilder info = new StringBuilder(getClassName(object.getClass().getName()) + "[");
            for (Field field : fields) {
                String fieldName = field.getName();
                if (getFieldType(field).equals("String")) {
                    info.append(fieldName + "=" + "\'" + field.get(object) + "\', ");
                } else {
                    info.append(fieldName + "=" + field.get(object) + ", ");
                }
            }
            if (info.length() >= 2) {
                info.delete(info.length() - 2, info.length());
            }
            info.append("]");
            return info.toString();
        }

    private static void updateObject() throws IllegalAccessException {
        System.out.println("---------------------\n" +
                "Enter name of the field for changing:");
        String fieldName = scanner.nextLine();
        for(Field field : fields){
            if(field.getName().equals(fieldName)){
                setField(field, true);
            }
        }
    }

    }
