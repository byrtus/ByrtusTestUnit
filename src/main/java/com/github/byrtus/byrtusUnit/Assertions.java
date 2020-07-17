package com.github.byrtus.byrtusUnit;

import java.util.Objects;

public class Assertions {


    public static void assertEquals (Object expected, Object actual){
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(String.format("Object --> '" + expected + "' <-- and --> '" + actual + "' <-- are not the same."));
        }
    }

    public static void assertTrue (Object expected){
        if (!Objects.equals(expected, true)) {
            throw new AssertionError(String.format("Object --> '" + expected + "' <--  is not assert 'True'."));
        }
    }

    public static void assertFalse (Object expected){
        if (!Objects.equals(expected, false)) {
            throw new AssertionError(String.format("Object --> '" + expected + "' <-- is not assert 'False'."));
        }
    }
}
