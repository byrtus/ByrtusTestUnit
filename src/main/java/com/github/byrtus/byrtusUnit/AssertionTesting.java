package com.github.byrtus.byrtusUnit;

import static com.github.byrtus.byrtusUnit.Assertions.*;

public class AssertionTesting {

    @Test
    public static void testEqualsToPassWithNulls() {
        assertEquals(null, null);
    }

    @Test
    public static void testEqualsToPassWithStrings() {
        assertEquals("Test", "Test");
    }

    @Test
    public static void testEqualsToFail(){
        assertEquals("test1", "test2");
    }

    @Test
    public static void testEqualsTrueToPass(){
        assertTrue(true);
    }

    @Test
    public static void testEqualsTrueToFail(){
        assertTrue(false);
    }

    @Test
    public static void testEqualsFalseToPass(){
        assertFalse(false);
    }

    @Test
    public static void testEqualsFalseToFail(){
        assertFalse(true);
    }
}
