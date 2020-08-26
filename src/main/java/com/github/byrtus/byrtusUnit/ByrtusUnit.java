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
                .filter(method -> method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(DependsOn.class) && method.getAnnotation(Test.class).name().equals(""))
                .forEach(ByrtusUnit::startTesting);

        // print finish titles
        printFinishTestingMenu();
    }

    public Method getMethodFromTestAnnotationFromItsName(Method[] methods, String testName) {
        for (Method methode : methods) {
            if (methode.isAnnotationPresent(Test.class)) {
                if (methode.getAnnotation(Test.class).name().equals(testName)) {
                    return methode;
                }
            }
        }
        return null;
    }

    private static void startTesting(Method method) {
        Throwable throwableException = null;
        Boolean isPassed = true;
        try {
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

    private void startTestingDependsOn(Method method) {
        Throwable throwableException = null;
        Boolean isPassed = true;
        List<String> listOfDependsOnMethods;

        //finding a list of methods that depends on current method
        listOfDependsOnMethods = getMethodDependsOnMethodsList(method, this.methods);

        //print methods depends on parent Method
        printListOfMethodsDependsOnParentMethod(method, listOfDependsOnMethods);


        if (method != null) {
            try {
                method.invoke(null);

            } catch (IllegalAccessException e) {
                isPassed = false;
                throwableException = e.getCause();
            } catch (InvocationTargetException e) {
                isPassed = false;
                throwableException = e.getCause();
            }
            printTestResult(throwableException, isPassed, method);

            //check if can enter and test method dependencies
            if (isPassed) {
                changeTestStatus(method.getAnnotation(Test.class).name(), "pass");
                for (String element : listOfDependsOnMethods) {
                    System.out.println("\nTake On Work Branch: " + element);
                    Method elementMethod = getMethodFromTestAnnotationFromItsName(this.methods, element);

                    if (!this.dependsOnTestsStatus.get(element).equals("fail")) {
                        startTestingChain(elementMethod);
                    }
                }
                System.out.println("\n' ' ' ' ' ' ' ' ' ' ' ' ' ' ' ' ' ' ' '\n");
            } else {
                changeTestStatus(method.getAnnotation(Test.class).name(), "fail");
            }
        } else {
            printMessageWhenNoDependsOnToTesting();
        }
    }

    private void startTestingChain(Method method) {
        List<String> childMethods;
        childMethods = getChildMethods(method);

        boolean ifAllReadyPassTest = isMethodPass(method);

        if (ifAllReadyPassTest) {
            List<Method> parents = getMethodDependsOnMethodsList(method);
            for (Method methodToCheck : parents) {
                startTestingChain(methodToCheck);
            }
        } else if (isMethodFailTest(method)) {
            System.out.println("Method: " + method.getAnnotation(Test.class).name() + " all ready fail test");
        } else {

            //check if method can do test
            if (canDependOnMethodCanStartTesting(childMethods)) {
                System.out.println("Can Testing");
                boolean ifPass = testMethod(method);

                if (ifPass) {
                    changeTestStatus(method.getAnnotation(Test.class).name(), "pass");
                    System.out.println("Next dependency branch");
                    List<Method> parents = getMethodDependsOnMethodsList(method);
                    for (Method methodToCheck : parents) {
                        startTestingChain(methodToCheck);
                    }

                } else {
                    changeTestStatus(method.getAnnotation(Test.class).name(), "fail");
                }

            } else {
                System.out.println(method.getAnnotation(Test.class).name() + " can't testing right now.");
            }
        }
    }

    private boolean isMethodFailTest(Method method) {
        boolean result = false;
        if (this.dependsOnTestsStatus.get(method.getAnnotation(Test.class).name()).equals("fail")) {
            return true;
        }
        return result;
    }

    private boolean isMethodPass(Method method) {
        boolean result = false;
        if (this.dependsOnTestsStatus.get(method.getAnnotation(Test.class).name()).equals("pass")) {
            return true;
        }
        return result;
    }

    private boolean testMethod(Method method) {
        Throwable throwableException = null;
        Boolean isPassed = true;

        if (method != null) {
            try {
                method.invoke(null);

            } catch (IllegalAccessException e) {
                isPassed = false;
                throwableException = e.getCause();
            } catch (InvocationTargetException e) {
                isPassed = false;
                throwableException = e.getCause();
            }
            printTestResult(throwableException, isPassed, method);
        } else {
            System.out.println("There is nothing to Test in DependsOn Annotation");
            isPassed = false;
        }
        return isPassed;
    }

    private boolean canDependOnMethodCanStartTesting(List<String> childMethods) {
        boolean ifCanTesting = true;
        for (String element : childMethods) {
            if (!this.dependsOnTestsStatus.get(element).equals("pass")) {
                ifCanTesting = false;
            }
        }
        return ifCanTesting;
    }

    private List<String> getChildMethods(Method method) {
        List<String> result = new ArrayList<>();
        for (String element : method.getAnnotation(DependsOn.class).value()) {
            result.add(element);
        }
        return result;
    }

    private void changeTestStatus(String testName, String status) {
        this.dependsOnTestsStatus.replace(testName, status);
    }

    private void startSetMapWithTests(Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                if (method != null & !method.getAnnotation(Test.class).name().equals("")) {
                    this.dependsOnTestsStatus.put(method.getAnnotation(Test.class).name(), "Can't Test");
                }
            }
        }
    }

    private void startSetRootMethodsList(Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                if (method != null & !method.getAnnotation(Test.class).name().equals("") & !method.isAnnotationPresent(DependsOn.class)) {
                    this.rootsMethodsList.add(method.getAnnotation(Test.class).name());
                }
            }
        }
    }

    private static List<String> getMethodDependsOnMethodsList(Method method, Method[] methods) {
        List<String> result = new ArrayList<>();

        for (Method element : methods) {

            if (element.isAnnotationPresent(DependsOn.class)) {

                for (String name : element.getAnnotation(DependsOn.class).value()) {

                    if (name.equals(method.getAnnotation(Test.class).name())) {
                        result.add(element.getAnnotation(Test.class).name());
                    }
                }
            }
        }
        return result;
    }

    private List<Method> getMethodDependsOnMethodsList(Method method) {
        List<Method> result = new ArrayList<>();

        for (Method element : this.methods) {

            if (element.isAnnotationPresent(DependsOn.class)) {

                for (String name : element.getAnnotation(DependsOn.class).value()) {

                    if (name.equals(method.getAnnotation(Test.class).name())) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }





    // methods for view UI

    private static void printTestResult(Throwable throwableException, boolean isPassed, Method method) {
        if (isPassed == true) {
            System.out.println("Method: '" + method.getName() + "' have PASS Test.\n" +
                    "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        } else {
            System.out.println("Method: '" + method.getName() + "' have FAIL Test.\n" +
                    "Reason: " + throwableException.getClass().getName() + "\n" +
                    "Message: " + throwableException.getMessage() + "\n" +
                    "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        }
    }
}
