package com.github.byrtus.byrtusUnit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ByrtusUnit {

    HashMap<String, String> dependsOnTestsStatus = new HashMap<>();
    ArrayList<String> rootsMethodsList = new ArrayList<>();
    Method[] methods;

    public void getTestAnnotationClasses(Class<?>... classes) {
        for (Class<?> thisClass : classes) {
            getMethodsFromClass(thisClass);
        }
    }

    public void getMethodsFromClass(Class<?> thisClass) {
        Method[] methods = thisClass.getMethods();
        this.methods = methods;

        // set List and map with methods status
        startSetMapWithTests(methods);
        startSetRootMethodsList(methods);

        //print menu
        printStartMenuForDependsOnTesting();

        // testing methods with DependsOn Annotation or connection to it
        // stream filter for root methods
        Arrays.stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(DependsOn.class) && !method.getAnnotation(Test.class).name().equals(""))
                .forEach(method -> startTestingDependsOn(method));

        //print final depends on testing result
        printFinalTestsResultForDependsOnAnnotations();

        //print menu
        printMenuForNormalTests();

        // testing methods WITH OUT ANY DependsOn Annotation and it connection
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
