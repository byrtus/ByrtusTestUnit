package com.github.byrtus.byrtusUnit;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello world\n");

        System.out.println("-----------------------------\n" +
                "  Testing Assertions Step 1\n" +
                "-----------------------------\n");

        new ByrtusUnit().getTestAnnotationClasses(AssertionTesting.class);

        System.out.println("-----------------------------\n" +
                "  Testing DependsOn Step 2\n" +
                "-----------------------------\n");

        new ByrtusUnit().getTestAnnotationClasses(DependsOnTesting.class);
    }
}
