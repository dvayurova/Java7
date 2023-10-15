package edu.school21.logic;

import java.lang.reflect.InvocationTargetException;

public class ObjectCreator {
    public static Object createEmptyObject(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> selectedClass = Class.forName("edu.school21.classes." + className);
        return selectedClass.getDeclaredConstructor().newInstance();
    }


}
