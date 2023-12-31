package edu.school21.program;

import edu.school21.manager.OrmManager;
import edu.school21.service.ClassGetter;
import edu.school21.service.FieldsSetter;
import edu.school21.service.ObjectCreator;
import edu.school21.service.Printer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Program {

    private static final String packageName = "edu.school21.model";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Class<?>[] classes = getClasses();
        Connection connection = connect();
        OrmManager ormManager = new OrmManager(connection, classes);
        try {
            runProgram(classes, scanner, ormManager);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runProgram(Class<?>[] classes, Scanner scanner, OrmManager ormManager) throws IllegalAccessException {
        String choice = "";
        System.out.println("Please, choose a class:");
        Object object = null;
        Printer.printClasses(classes);
        String className = scanner.nextLine();
        try {
            object = ObjectCreator.createEmptyObject(className);
            ormManager.initializeTable(object);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
            System.exit(-1);
        }
        while (!choice.equals("exit")) {
            printSelection();
            choice = scanner.nextLine();
            switch (choice) {
                case "save":
                    FieldsSetter.setObjectFields(scanner, object);
                    ormManager.save(object);
                    break;
                case "update":
                    updateObject(object.getClass(), scanner, ormManager);
                    break;
                case "findById":
                    Object foundedObj = findById(object.getClass(), scanner, ormManager);
                    System.out.println(foundedObj);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Incorrect choice, try again.");
            }
        }
    }

    private static void updateObject(Class<?> aClass, Scanner scanner, OrmManager ormManager){
        Object object = findById(aClass, scanner, ormManager);
        try {
            FieldsSetter.updateObject(scanner, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ormManager.update(object);
    }

    private static Object findById(Class<?> aClass, Scanner scanner, OrmManager ormManager){
        System.out.println("Please enter id:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        Object object = null;
        try {
            object = ormManager.findById(id, aClass);
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.err.println("Failed to find. Check if id is correct and try again. " + e.getMessage());
        }
        return object;
    }


    private static Connection connect() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] getClasses() {
        try {
            return (Class<?>[]) ClassGetter.getClasses(packageName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static void printSelection() {
        System.out.println("\nSelect an operation:\n - save\n - update \n - findById\n - exit (finish the program execution)");
    }

}
