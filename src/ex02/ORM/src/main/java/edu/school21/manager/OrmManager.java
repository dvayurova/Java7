package edu.school21.manager;

import edu.school21.annotations.OrmEntity;
import edu.school21.service.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OrmManager {
    private final Connection connection;
    private final Class<?>[] classes;

    public OrmManager(Connection connection, Class<?>[] classes) {
        this.connection = connection;
        this.classes = classes;
    }

    public void tmp() {


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

    public void initializeTable(Object object) {
        dropAllTables(classes);
        if (object.getClass().isAnnotationPresent(OrmEntity.class)) {
            try {
                executeSqlQuery(QueryCreator.createTableQuery(object.getClass()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void dropAllTables(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(OrmEntity.class)) {
                executeSqlQuery(QueryCreator.dropTableQuery(clazz));
            }
        }
    }

    private void executeSqlQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            Printer.printQuery(query);
            if (query.contains("SELECT")) {
                statement.executeQuery(query);
            } else {
                statement.executeUpdate(query);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Object entity) {
        try {
            String query = QueryCreator.saveQuery(entity);
            Printer.printQuery(query);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Field field = entity.getClass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity, resultSet.getLong(1));
            } else {
                throw new SQLException("Failed to retrieve generated keys for message");
            }

        } catch (IllegalAccessException | SQLException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Object entity) {
        try {
            executeSqlQuery(QueryCreator.updateQuery(entity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
