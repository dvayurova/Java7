package edu.school21.manager;

import edu.school21.annotations.OrmEntity;
import edu.school21.service.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OrmManager {

    private static DataSource dataSource;
    private static Connection connection;
    private static final String packageName = "edu.school21.model";


    public static void main(String[] args) {
        Class<?>[] classes;
        try {
            classes = ClassGetter.getClasses(packageName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Object object;
        Printer.printClasses(classes);
        Scanner scanner = new Scanner(System.in);
        String className = scanner.nextLine();
        try {
            object = ObjectCreator.createEmptyObject(className);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("error: " + e.getMessage());
            return;
        }
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initializeTable(object, classes);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO simple_product (productName, price) VALUES ('milk', 80);");
            statement.executeUpdate("INSERT INTO simple_product (productName, price) VALUES ('onion', 50);");
            statement.executeUpdate("INSERT INTO simple_product (productName, price) VALUES ('cheese', 200);");
            statement.executeUpdate("INSERT INTO simple_product (productName, price) VALUES ('water', 30);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM simple_product;");

            System.out.println("here.");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("productName") + " " + resultSet.getInt("price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeTable(Object object, Class<?>[] classes) {
        dropAllTables(classes);
        if (object.getClass().isAnnotationPresent(OrmEntity.class)) {
            try {
                createTable(object.getClass());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static void createTable(Class<?> clazz){
        String query = QueryCreator.createTableQuery(clazz);
        Printer.printQuery(query);
        executeSqlQuery(query);
    }

    private static void dropAllTables(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(OrmEntity.class)) {
                String query = QueryCreator.dropTableQuery(clazz);
                Printer.printQuery(query);
                executeSqlQuery(query);
            }
        }
    }

    private static void executeSqlQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            if (query.contains("SELECT")) {
                statement.executeQuery(query);
            } else {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
