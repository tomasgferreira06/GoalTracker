package com.example.gps_g12.calculator;

import com.example.gps_g12.goalTracker.model.data.Date;

import java.lang.Math;
public class Calculator {

    private double result;
    private String operation;
    private double num1;
    private double num2;

    private boolean nextNumberIsNum1 = true;

    private boolean isDate = false;
    private boolean isDate2 = false;

    //Date related atributtes
    private Date date;
    private Date date2;

    private Date dateResult;
    private boolean isTypingDay = false;
    private boolean isTypingMonth = false;
    private boolean isTypingYear = false;

    //HEX
    private String hex;

    //Constructor
    public Calculator(){
        this.result = 0;
        this.operation = "";
        this.num1 = 0;
        this.num2 = 0;
        this.date = null;
        this.date2 = null;
        this.dateResult = null;
        this.hex = "";
    }

    //Operations
    public double sqrt(double num1){
        this.result = Math.sqrt(num1);
        return result;
    }


    public double sum(double num1,double num2){
        this.result = num1 + num2;
        return result;
    }

    public double min(double num1,double num2){
        this.result = num1 - num2;
        return result;
    }

    public double mul(double num1,double num2){
        this.result = num1 * num2;
        return result;
    }

    public double div(double num1, double num2){
        this.result = num1 / num2;
        return result;
    }

    public double cylinder(double num1, double num2){
        this.result = Math.PI*num2*num2*num1;
        return result;
    }

    public double cone(double num1, double num2){
        this.result = (Math.PI*num2*num2*num1)*1/3;
        return result;
    }

    public double factorial(int num) {
        if (num == 0 || num == 1) {
            return 1;
        } else {
            double result = 1;
            for (int i = 2; i <= num; i++) {
                result *= i;
            }
            return result;
        }
    }

    public int fibonacci(int num) {
        if (num == 0) {
            return 0;
        } else if (num == 1) {
            return 1;
        } else {
            int fib1 = 0;
            int fib2 = 1;
            int fibonacci = 0;
            for (int i = 2; i <= num; i++) {
                fibonacci = fib1 + fib2;
                fib1 = fib2;
                fib2 = fibonacci;
            }
            return fibonacci;
        }
    }

    public Date dateSumDays(Date date, double num1){

        int totalDays = (int) num1;
        int daysToAdd = totalDays;
        int newDay = date.getDay();
        int newMonth = date.getMonth();
        int newYear = date.getYear();

        while (daysToAdd > 0) {
            int daysInMonth = getDaysInMonth(newYear, newMonth);
            int remainingDaysInMonth = daysInMonth - newDay + 1;

            if (remainingDaysInMonth <= daysToAdd) {
                daysToAdd -= remainingDaysInMonth;
                newDay = 1;
                newMonth++;

                if (newMonth > 12) {
                    newMonth = 1;
                    newYear++;
                }
            } else {
                newDay += daysToAdd;
                daysToAdd = 0;
            }
        }

        return new  Date(newDay, newMonth, newYear);

    }

    public Date dateSubtractDays(Date date, double numDays) {
        int totalDays = (int) numDays;
        int daysToSubtract = totalDays;
        int newDay = date.getDay();
        int newMonth = date.getMonth();
        int newYear = date.getYear();

        while (daysToSubtract > 0) {
            if (newDay <= daysToSubtract) {
                if (newMonth == 1) {
                    newYear--;
                    newMonth = 12;
                } else {
                    newMonth--;
                }
                int daysInPreviousMonth = getDaysInMonth(newYear, newMonth);
                newDay = daysInPreviousMonth + newDay - daysToSubtract;
                daysToSubtract -= newDay - 1;
            } else {
                newDay -= daysToSubtract;
                daysToSubtract = 0;
            }
        }

        return new Date(newDay, newMonth, newYear);
    }

    public int dateMinDate(Date date1, Date date2){
        int days1 = date1.getYear() * 365 + date1.getMonth() * 30 + date1.getDay();
        int days2 = date2.getYear() * 365 + date2.getMonth() * 30 + date2.getDay();

        return Math.abs(days1 - days2);
    }

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29; // Ano bissexto
                } else {
                    return 28; // Não é um ano bissexto
                }
            default:
                return 31;
        }
    }

    public String doubleToHexadecimal(double value) {
        // Converte o valor double para uma representação binária
        //long longValue = Double.doubleToRawLongBits(value);
        //String binaryValue = Long.toBinaryString(longValue);
        int binaryValueInt = (int) value;
        String binaryValue = Integer.toString(binaryValueInt);

        // Converte a representação binária para um valor inteiro
        int intValue = Integer.parseInt(binaryValue, 2);

        // Converte o valor inteiro para hexadecimal
        String hexadecimalValue = Integer.toHexString(intValue).toUpperCase();

        return hexadecimalValue;
    }



    public String hexadecimalToBinary(String hexadecimal) {
        try {
            // Converte a representação hexadecimal em um valor decimal
            int decimalValue = Integer.parseInt(hexadecimal, 16);

            // Converte o valor decimal em uma representação binária
            String binaryValue = Integer.toBinaryString(decimalValue);

            return binaryValue;
        } catch (NumberFormatException e) {
            // Se houver um erro ao converter, retorne um valor de erro
            return "Erro na conversão";
        }
    }



    //Getters & Setters
    public void reset(){
        this.result = 0;
        this.num1 = 0;
        this.num2 = 0;
        this.operation = "";
        this.nextNumberIsNum1 = true;
        this.isDate = false;
        this.isDate2 = false;
        this.date = null;
        this.date2 = null;
        this.dateResult = null;
        this.hex = "";
    }

    //HEX getter and setter
    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public double getResult() {
        return this.result;
    }

    public void setResult(double result){
        this.result = result;
    }

    public void setDateResult(Date dateResult){
        this.dateResult = dateResult;
    }

    public String getDateResultAsString(){
        return dateResult.getDay() + "/" + dateResult.getMonth() + "/" + dateResult.getYear();
    }

    public String getOperation(){
        return this.operation;
    }

    public void setOperation(String operation){
        this.operation = operation;
    }

    public double getNum1(){
        return this.num1;
    }

    public double getNum2(){
        return this.num2;
    }

    public void setNum1(double num1){
        this.num1 = num1;
    }

    public void setNum2(double num2){
        this.num2 = num2;
    }

    public void setNextNumberIsNum1(boolean bool){this.nextNumberIsNum1 = bool;}

    public boolean getIsNextNumberIsNum1(){return this.nextNumberIsNum1;}

    public void createDate(){
        this.date = new Date();
    }

    public Date getDate(){return this.date;}

    public Date getDate2(){return this.date2;}

    public void setDate2(Date newDate){this.date2 = newDate;}

    public String getDateAsString(){
        return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
    }

    public String getDate2AsString(){
        return date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear();
    }

    public void setIsDate(boolean bool) {
        isDate = bool;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setIsDate2(boolean bool){isDate2 = bool;}

    public boolean isDate2(){return this.isDate2;}

    public boolean isTypingDay() {
        return isTypingDay;
    }

    public void setIsTypingDay(boolean typingDay) {
        isTypingDay = typingDay;
    }

    public boolean isTypingMonth() {
        return isTypingMonth;
    }

    public void setIsTypingMonth(boolean typingMonth) {
        isTypingMonth = typingMonth;
    }

    public boolean isTypingYear() {
        return isTypingYear;
    }

    public void setIsTypingYear(boolean typingYear) {
        isTypingYear = typingYear;
    }
}
