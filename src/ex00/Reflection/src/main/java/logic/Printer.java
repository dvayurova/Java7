package logic;

import classes.Car;
import classes.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Printer {
    public static void printClasses() {
        System.out.println("Classes:");
        String userClass = User.class.getName();
        String carClass = Car.class.getName();
        System.out.println(" - " + ObjectInfo.getClassName(userClass));
        System.out.println(" - " + ObjectInfo.getClassName(carClass));
        System.out.println("---------------------\nEnter class name:");
    }


    public static void printFields(Field[] fields) {
        System.out.println("---------------------");
        System.out.println("fields:");
        for (Field field : fields) {
            System.out.println("\t\t" + ObjectInfo.getFieldType(field) + " " + field.getName());
        }
    }

    public static void printMethods(Method[] methods) {
        System.out.println("methods:");
        for (Method method : methods) {
            String methodName = method.getName();
            if (isInheritedObjectMethods(methodName)) continue;
            String type = method.getReturnType().getName();
            type = type.contains("lang.") ? type.substring(type.indexOf("lang.") + 5) : type;
            System.out.println("\t\t" + type + " " + methodName + ObjectInfo.getMethodParameters(method));
        }
        System.out.println("---------------------");
    }

    private static boolean isInheritedObjectMethods(String methodName) {
        return (methodName.equals("toString") || methodName.equals("wait") || methodName.equals("equals") || methodName.equals("hashCode") || methodName.equals("getClass") || methodName.equals("notify") || methodName.equals("notifyAll"));
    }

    public static void printCreateInfo(ObjectInfo objectInfo, Object object) {
        try {
            System.out.println("Object created: " + objectInfo.getObjectInfo(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printUpdateInfo(ObjectInfo objectInfo, Object object) {
        try {
            System.out.println("Object updated: " + objectInfo.getObjectInfo(object));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


}
