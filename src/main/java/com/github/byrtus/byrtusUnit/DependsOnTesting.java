package com.github.byrtus.byrtusUnit;

import static com.github.byrtus.byrtusUnit.Assertions.assertEquals;

public class DependsOnTesting {


    @Test (name = "test1")
    public static void testEqualsToPass1(){
        assertEquals("TestPass", "TestPass");
    }

    @DependsOn (value = {"test1"})
    @Test (name = "test2")
    public static void testEqualsToPass2() {
        assertEquals("TestPass", "TestPass");
    }

    @DependsOn (value = {"test1", "test5"})
    @Test (name = "test3")
    public static void testEqualsToPass3() {
        assertEquals("TestPass", "TestPass");
    }
    @DependsOn (value = {"test2", "test1", "test3"})
    @Test (name = "test4")
    public static void testEqualsToPass4() {
        assertEquals("TestPass", "TestPass");
    }

    @Test (name = "test5")
    public static void testEqualsToPass5() {
        assertEquals("TestPass", "TestPass");
    }

    @Test
    public static void testEqualsToPass6() {
        assertEquals("TestPass", "TestPass");
    }

    @Test
    public static void testEqualsToPass7() {
        assertEquals("TestPass", "TestPass");
    }

    @Test
    public static void testEqualsToFail8() {
        assertEquals("Test", "Fail");
    }
}

