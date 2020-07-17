package com.github.byrtus.byrtusUnit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ByrtusUnit {


    public void getTestAnnotationClasses(Class<?>... classes) {
        for (Class<?> thisClass: classes) {
            getMethodsFromClass(thisClass);
        }
    }

    public void getMethodsFromClass(Class<?> thisClass) {
        Method[] methods = thisClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.isAnnotationPresent(Test.class))
                .forEach(ByrtusUnit :: startTesting);
    }

    private static void startTesting (Method method){
        Throwable throwableException = null;
        Boolean isPassed = true;
        try{
            method.invoke(null);
        } catch (IllegalAccessException e) {
            isPassed = false;
            throwableException = e.getCause();
        } catch (InvocationTargetException e) {
            isPassed = false;
            throwableException = e.getCause();
        }
        printTestResult(throwableException, isPassed, method);
    }

    private static void printTestResult(Throwable throwableException, boolean isPassed, Method method){
        if(isPassed == true){
            System.out.println("Method: '" + method.getName() + "' have PASS Test.\n" +
                    "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        }else {
            System.out.println("Method: '" + method.getName() + "' have FAIL Test.\n" +
                    "Reason: " + throwableException.getClass().getName() + "\n" +
                    "Message: " + throwableException.getMessage() + "\n" +
                    "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        }
    }
}
