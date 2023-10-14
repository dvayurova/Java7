package logic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MethodCaller {

    public static void callMethod(Method[] methods, Scanner scanner, Object object) throws InvocationTargetException, IllegalAccessException {
        System.out.println("---------------------\n" +
                "Enter name of the method for call:");
        String methodName = scanner.nextLine();
        for (Method method : methods) {
            String fullMethodName = methodName + ObjectInfo.getMethodParameters(method);
            if (fullMethodName.startsWith(method.getName())) {
                Object[] params = getParametersForMethodCall(method, scanner);
                Object result = method.invoke(object, params);
                if (!method.getReturnType().getName().contains("void"))
                    System.out.println("Method returned:\n" + result);
                return;
            }
        }

    }

    private static Object[] getParametersForMethodCall(Method method, Scanner scanner) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parametersTypes = method.getParameterTypes();
        Object[] parameters = new Object[parametersTypes.length];
        for (int i = 0; i < parametersTypes.length; i++) {
            Class<?> parameterType = parametersTypes[i];
            if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
                System.out.println("Enter int value:");
                parameters[i] = scanner.nextInt();
            } else if (parameterType.equals(String.class)) {
                System.out.println("Enter int value:");
                parameters[i] = scanner.nextLine();
            } else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
                System.out.println("Enter int value:");
                parameters[i] = scanner.nextDouble();
            } else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
                System.out.println("Enter int value:");
                parameters[i] = scanner.nextLong();
            } else if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class)) {
                System.out.println("Enter int value:");
                parameters[i] = scanner.nextBoolean();
            }
        }
        return parameters;
    }
}
