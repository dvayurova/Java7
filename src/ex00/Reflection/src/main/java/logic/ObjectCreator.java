package logic;

import java.lang.reflect.InvocationTargetException;

public class ObjectCreator {
    public static Object createEmptyObject(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> selectedClass = Class.forName("classes." + className);
        return selectedClass.getDeclaredConstructor().newInstance();
    }


}
