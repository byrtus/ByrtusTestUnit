package com.github.byrtus.byrtusUnit;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello world\n");
        new ByrtusUnit().getMethodsFromClass(AssertionTesting.class);
    }
}
