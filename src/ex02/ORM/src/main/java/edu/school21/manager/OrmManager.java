package edu.school21.manager;

import edu.school21.annotations.OrmColumn;
import edu.school21.annotations.OrmColumnId;
import edu.school21.annotations.OrmEntity;
import edu.school21.service.ClassGetter;
import edu.school21.service.ObjectCreator;
import edu.school21.service.Printer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OrmManager {

    private static DataSource dataSource;
    private static Connection connection;


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
        dataSource = (DataSource) new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initializeTables(object);

//        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate("INSERT INTO simple_product (productName, price) VALUES ('milk', 80);");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try (Statement statement = connection.createStatement()) {
//            statement.executeQuery("SELECT * FROM simple_product;");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void initializeTables(Object object) {
        if (object.getClass().isAnnotationPresent(OrmEntity.class)) {
            try {
                createTable(object.getClass());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


    private static void createTable(Class<?> clazz) throws Exception {
        OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + ormEntity.table() + " (");
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumn.class)) {
                OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                String columnName = ormColumn.name();
                String fieldType = getSqlType(field.getType().getSimpleName());
                System.out.println("columnName = " + columnName);
                System.out.println("fieldType = " + fieldType);

                if (field.isAnnotationPresent(OrmColumnId.class)) {
//                    hasIdColumn = true;
                    sql.append(columnName).append(" ").append(fieldType).append(" AUTO_INCREMENT PRIMARY KEY, ");
                } else {
                    sql.append(columnName).append(" ").append(fieldType).append(", ");
                }
            }
        }


        sql.delete(sql.length() - 2, sql.length()); // Remove the trailing comma and space
        sql.append(")");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql.toString());
            System.out.println("Table created for class: " + clazz.getName());
        }
    }

    private static String getSqlType(String type) {
        switch (type) {
            case "String":
                return "VARCHAR(100) NOT NULL";
            case "Integer":
                return "INTEGER";
            default:
                return "";
        }
    }

}
