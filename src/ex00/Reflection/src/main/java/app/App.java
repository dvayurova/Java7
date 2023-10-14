package app;

import logic.*;


import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;


public class App {

    public static void main(String[] args) {
        Object object;
        Printer.printClasses();
        Scanner scanner = new Scanner(System.in);
        String className = scanner.nextLine();
        try {
            object = ObjectCreator.createEmptyObject(className);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
            return;
        }
        ObjectInfo objectInfo = new ObjectInfo(object);
        Printer.printFields(objectInfo.getFields());
        Printer.printMethods(objectInfo.getMethods());
        try {
            FieldsSetter.setObjectFields(objectInfo.getFields(), scanner, object);
            Printer.printCreateInfo(objectInfo, object);
            FieldsSetter.updateObject(objectInfo.getFields(), scanner, object);
            Printer.printUpdateInfo(objectInfo, object);
            MethodCaller.callMethod(objectInfo.getMethods(), scanner, object);
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}
