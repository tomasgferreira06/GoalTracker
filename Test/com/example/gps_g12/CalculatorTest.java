package com.example.gps_g12;

import com.example.gps_g12.calculator.Calculator;
import com.example.gps_g12.calculator.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calculator = new Calculator();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sqrt() {
    }

    @Test
    void sum() {
        assertEquals(1.0, calculator.sum(0, 1));
    }

    @Test
    void min() {
    }

    @Test
    void mul() { assertEquals(6.0, calculator.mul(2, 3)); }


    @Test
    void div() {
    }

    @Test
    void cylinder() { assertEquals(Math.PI, calculator.cylinder(1, 1)); }

    @Test
    void dateSumDays(){
        Date resultDate = new Date(18, 8, 2001);
        Date resultDate1 = new Date(25, 9 ,2028);
        assertEquals(resultDate, calculator.dateSumDays(new Date(10, 5, 2001), 100));
        assertEquals(resultDate1, calculator.dateSumDays(new Date(10, 5, 2001), 10000));
    }

    @Test
    void dateSubtractDays(){
        Date resultDate = new Date(30, 4, 2001);
        assertEquals(resultDate, calculator.dateSubtractDays(new Date(10, 5, 2001), 10));
    }

    @Test
    void dateMinDate(){
        Date date1 = new Date(10, 5, 2001);
        Date date2 = new Date(20, 4, 2001);


        assertEquals(20, calculator.dateMinDate(date1, date2));
    }

    @Test
    void dateMinDate2(){ //Testing when the first date is minor than the second (result would be negative but function is returning abs so its fine!!)
        Date date1 = new Date(10, 5, 2001);
        Date date2 = new Date(20, 4, 2001);
        
        assertEquals(20, calculator.dateMinDate(date2, date1));
    }

    @Test
    void doubleToBinaryDoubleToHexadecimal(){
        assertEquals("2A", calculator.doubleToHexadecimal(101010.0));
    }

    @Test
    void hexadecimalToBinary(){
        assertEquals("101010", calculator.hexadecimalToBinary("2A"));
    }

    @Test
    void cone(){assertEquals(Math.PI / 1/3, calculator.cone(1, 1));}

    @Test
    void fibonacci(){assertEquals(2,calculator.fibonacci(3));}

    @Test
    void factorial(){assertEquals(24,calculator.factorial(4));}
}