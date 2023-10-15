package edu.school21.logic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectInfo {

    private  Field[] fields;
    private  Method[] methods;


    public ObjectInfo( Object object) {
        fields = object.getClass().getDeclaredFields();
        methods = object.getClass().getMethods();
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
    }


    public static String getMethodParameters(Method method) {
        StringBuilder params = new StringBuilder("(");
        Class<?>[] parametersTypes = method.getParameterTypes();
        for (int i = 0; i < parametersTypes.length; i++) {
            params.append(parametersTypes[i].getTypeName() + ", ");
        }
        if (params.length() >= 2) {
            params.delete(params.length() - 2, params.length());
        }
        params.append(")");
        return params.toString();
    }

    public static String getFieldType(Field field) {
        String type = field.getType().getName();
        return type.contains("lang.") ? type.substring(type.indexOf("lang.") + 5) : type;
    }

    public static String getClassName(String className) {
        return className.substring(className.indexOf("classes.") + 8);
    }

    public  String getObjectInfo(Object object) throws IllegalAccessException {
        StringBuilder info = new StringBuilder(ObjectInfo.getClassName(object.getClass().getName()) + "[");
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


}
