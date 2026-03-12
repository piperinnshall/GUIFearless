package com.piperinnshall.fluentguijava.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTest {

    @Test
    public void additionWorks() {
        assertEquals(4, 2 + 2, "2 + 2 should equal 4");
    }

    @Test
    public void stringConcatWorks() {
        String result = "Hello, " + "JUnit";
        assertEquals("Hello, JUnit", result);
    }
} 
