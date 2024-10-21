package com.example.gps_g12.calculator;

import com.example.gps_g12.goalTracker.model.data.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorController {

    Calculator calculator = new Calculator();
    @FXML
    private Label result;

    @FXML
    protected void onLetterClick(ActionEvent event){
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        if(calculator.getOperation().equalsIgnoreCase("hexToBin")){
            calculator.setHex(calculator.getHex() + buttonText);
            result.setText("HexToBin: " + calculator.getHex());
        }
    }

    @FXML
    protected void onNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        /*if(calculator.getOperation().equalsIgnoreCase("binToHex")){
            StringBuilder sb = new StringBuilder(Integer.toString((int) calculator.getNum1()));
            sb.append(buttonText);
            calculator.setNum1(Double.parseDouble(sb.toString()));
        }*/
        if(calculator.getOperation().equalsIgnoreCase("binToHex")){
            if(!buttonText.equalsIgnoreCase("0") && !buttonText.equalsIgnoreCase("1")){
                result.setText("BinToHex: " + calculator.getNum1());
                return;
            }
        }

        Date date = calculator.getDate();
        Date date2 = calculator.getDate2();
        if(calculator.isDate()){
            if(calculator.isTypingDay()){

                if(date.getDay() == 0 && !date.getWasZeroTyped()){
                    if(!buttonText.equals("0")){
                        date.setDay(Integer.parseInt(buttonText));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
                        return;
                    }else{
                        date.setWasZeroTyped(true);
                        result.setText("0" + "/" + date.getMonth() + "/" + date.getYear());
                        return;
                    }
                }else{
                    if(date.getWasZeroTyped() && date.getDay() == 0){
                        date.setDay(Integer.parseInt(buttonText));
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }else if(date.getWasZeroTyped() && date.getDay() != 0){
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        date.setDay(Integer.parseInt(date.getDay() + "0"));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }else if(date.getDay() != 0 && !date.getWasZeroTyped()){
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        date.setDay(Integer.parseInt(date.getDay() + buttonText));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }
                }

            }

            if(calculator.isTypingMonth()){

                if(date.getMonth() == 0 && !date.getWasZeroTyped()){
                    if(!buttonText.equals("0")){
                        date.setMonth(Integer.parseInt(buttonText));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
                        return;
                    }else{
                        date.setWasZeroTyped(true);
                        result.setText(date.getDay() + "/" + "0" + "/" + date.getYear());
                        return;
                    }
                }else{
                    if(date.getWasZeroTyped() && date.getMonth() == 0){
                        date.setMonth(Integer.parseInt(buttonText));
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }else if(date.getWasZeroTyped() && date.getMonth() != 0){
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        date.setMonth(Integer.parseInt(date.getMonth() + "0"));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }else if(date.getMonth() != 0 && !date.getWasZeroTyped()){
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        date.setMonth(Integer.parseInt(date.getMonth() + buttonText));
                        result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());

                        date.setWasZeroTyped(false);
                        return;
                    }
                }

            }

            if(calculator.isTypingYear()){
                if(date.getYear() == 0 && buttonText.equals("0")){
                    return;
                }

                if(Integer.toString(date.getYear()).length() == 4){
                    return;
                }

                StringBuilder sb = new StringBuilder("" + date.getYear());
                sb.append(buttonText);

                int yearValue = Integer.parseInt(sb.toString());
                date.setYear(yearValue);

                result.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
                return;
            }
        }else if(calculator.isDate2()){
            if(calculator.isTypingDay()){

                if(date2.getDay() == 0 && !date2.getWasZeroTyped()){
                    if(!buttonText.equals("0")){
                        date2.setDay(Integer.parseInt(buttonText));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());
                        return;
                    }else{
                        date2.setWasZeroTyped(true);
                        result.setText(calculator.getDateAsString() + " - " + "0" + "/" + date2.getMonth() + "/" + date2.getYear());
                        return;
                    }
                }else{
                    if(date2.getWasZeroTyped() && date2.getDay() == 0){
                        date2.setDay(Integer.parseInt(buttonText));
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }else if(date2.getWasZeroTyped() && date2.getDay() != 0){
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        date2.setDay(Integer.parseInt(date2.getDay() + "0"));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }else if(date2.getDay() != 0 && !date2.getWasZeroTyped()){
                        calculator.setIsTypingDay(false);
                        calculator.setIsTypingMonth(true);

                        date2.setDay(Integer.parseInt(date2.getDay() + buttonText));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }
                }

            }

            if(calculator.isTypingMonth()){

                if(date2.getMonth() == 0 && !date2.getWasZeroTyped()){
                    if(!buttonText.equals("0")){
                        date2.setMonth(Integer.parseInt(buttonText));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());
                        return;
                    }else{
                        date2.setWasZeroTyped(true);
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + "0" + "/" + date2.getYear());
                        return;
                    }
                }else{
                    if(date2.getWasZeroTyped() && date2.getMonth() == 0){
                        date2.setMonth(Integer.parseInt(buttonText));
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }else if(date2.getWasZeroTyped() && date2.getMonth() != 0){
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        date2.setMonth(Integer.parseInt(date2.getMonth() + "0"));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }else if(date2.getMonth() != 0 && !date2.getWasZeroTyped()){
                        calculator.setIsTypingMonth(false);
                        calculator.setIsTypingYear(true);

                        date2.setMonth(Integer.parseInt(date2.getMonth() + buttonText));
                        result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());

                        date2.setWasZeroTyped(false);
                        return;
                    }
                }

            }

            if(calculator.isTypingYear()){
                if(date2.getYear() == 0 && buttonText.equals("0")){
                    return;
                }

                if(Integer.toString(date2.getYear()).length() == 4){
                    return;
                }

                StringBuilder sb = new StringBuilder("" + date2.getYear());
                sb.append(buttonText);

                int yearValue = Integer.parseInt(sb.toString());
                date2.setYear(yearValue);

                result.setText(calculator.getDateAsString() + " - " + date2.getDay() + "/" + date2.getMonth() + "/" + date2.getYear());
                return;
            }
        }

        if (calculator.getIsNextNumberIsNum1()) {
            String numText = Double.toString(calculator.getNum1());
            StringBuilder sb = new StringBuilder(numText);

            int decimalIndex = numText.indexOf(".");
            if (decimalIndex >= 0) {
                sb.insert(decimalIndex, buttonText);
            } else {
                sb.append(buttonText);
            }

            double numValue = Double.parseDouble(sb.toString());
            calculator.setNum1(numValue);
        } else {
            String numText = Double.toString(calculator.getNum2());
            StringBuilder sb = new StringBuilder(numText);

            int decimalIndex = numText.indexOf(".");
            if (decimalIndex >= 0) {
                sb.insert(decimalIndex, buttonText);
            } else {
                sb.append(buttonText);
            }

            double numValue = Double.parseDouble(sb.toString());
            calculator.setNum2(numValue);
        }

        if (!calculator.getOperation().equalsIgnoreCase("")) {
            if (calculator.getOperation().equalsIgnoreCase("sqrt"))
                result.setText("sqrt( " + calculator.getNum1() + " )");
            else if (calculator.getOperation().equalsIgnoreCase("sum")) {
                result.setText(calculator.getNum1() + " + " + calculator.getNum2());
            }
            else if (calculator.getOperation().equalsIgnoreCase("mul")) {
                result.setText(calculator.getNum1() + " * " + calculator.getNum2());
            }else if(calculator.getOperation().equalsIgnoreCase("div")){
                result.setText(calculator.getNum1() + " / " + calculator.getNum2());
            }else if(calculator.getOperation().equalsIgnoreCase("min")){
                result.setText(calculator.getNum1() + " - " + calculator.getNum2());
            }else if(calculator.getOperation().equalsIgnoreCase("cylinder")){
                result.setText("height = " + calculator.getNum1() + "\n" + "radius = " + calculator.getNum2());
            }else if (calculator.getOperation().equalsIgnoreCase("factorial")) {
                result.setText("(" + calculator.getNum1() + ")!");
            }else if(calculator.getOperation().equalsIgnoreCase("cone")) {
                result.setText("height = " + calculator.getNum1() + "\n" + "radius = " + calculator.getNum2());
            }else if(calculator.getOperation().equalsIgnoreCase("fibonacci")) {
                result.setText("F( " + calculator.getNum1() + " )");
            }else if(calculator.getOperation().equalsIgnoreCase("dateSumDays")){
                result.setText(calculator.getDateAsString() + " + " + calculator.getNum1());
            }else if(calculator.getOperation().equalsIgnoreCase("dateMinDays")){
                result.setText(calculator.getDateAsString() + " - " + calculator.getNum1());
            }else if(calculator.getOperation().equalsIgnoreCase("dateMinDate")){
                result.setText(calculator.getDateAsString() + " - " + calculator.getDate2AsString());
            }else if(calculator.getOperation().equalsIgnoreCase("binToHex")){
                result.setText("BinToHex: " + calculator.getNum1());
                return;
            }if(calculator.getOperation().equalsIgnoreCase("hexToBin")){
                calculator.setHex(calculator.getHex() + buttonText);
                result.setText("HexToBin: " + calculator.getHex());
            }

            else
                result.setText("" + calculator.getNum1());
        } else {
            System.out.println("2");
            result.setText("" + calculator.getNum1());
        }
    }

    //reset number on operation
    @FXML
    protected void onSqrtButtonClick(){
        calculator.reset();
        calculator.setOperation("sqrt");
        result.setText("sqrt( " + "num" + " )");
    }

    @FXML
    protected void onSumButtonClick(){
        if(calculator.isDate()){
            calculator.setIsDate(false);
            calculator.setOperation("dateSumDays");
            result.setText(calculator.getDateAsString() + " + ");
            return;
        }

        calculator.setOperation("sum");
        result.setText("+");
        calculator.setNextNumberIsNum1(false);
    }

    @FXML
    protected void onMulButtonClick(){
        calculator.setOperation("mul");
        result.setText("*");
        calculator.setNextNumberIsNum1(false);
    }

    @FXML
    protected void onDivButtonClick(){
        calculator.setOperation("div");
        result.setText("/");
        calculator.setNextNumberIsNum1(false);
    }


    @FXML
    protected void onMinButtonClick(){
        if(calculator.isDate()){
            calculator.setIsDate(false);
            calculator.setOperation("dateMinDays");
            result.setText(calculator.getDateAsString() + " - ");
            return;
        }

        calculator.setOperation("min");
        result.setText("-");
        calculator.setNextNumberIsNum1(false);
    }

    @FXML
    protected void onCylButtonClick(){
        //calculator.reset();
        calculator.setOperation("cylinder");
        result.setText("height = " + calculator.getNum1() + "\n" + "radius = " + "num");
        calculator.setNextNumberIsNum1(false);
    }

    @FXML
    protected void onConeButtonClick(){
        //calculator.reset();
        calculator.setOperation("cone");
        result.setText("height = " + calculator.getNum1() + "\n" + "radius = " + "num");
        calculator.setNextNumberIsNum1(false);
    }

    @FXML
    protected void onFactorialButtonClick() {
        calculator.reset();
        int num = (int) calculator.getNum1();
        result.setText("(" + "num" + ")!");
        double result = calculator.factorial(num);
        calculator.setResult(result);
        calculator.setOperation("factorial");
    }

    @FXML
    protected void onFibonacciButtonClick() {
        calculator.reset();
        int num = (int) calculator.getNum1();
        result.setText("F(" + "num" + ")");
        double result = calculator.fibonacci(num);
        calculator.setResult(result);
        calculator.setOperation("fibonacci");
    }

    @FXML
    protected void onDateButtonClick(){
        if(calculator.getDate() != null){
            calculator.setDate2(new Date());
            calculator.setIsDate2(true);
            calculator.setIsTypingDay(true);

            if(calculator.getOperation().equalsIgnoreCase("dateSumDays")){
                result.setText("ERROR: Can't Sum Dates");
                calculator.reset();
                return;
            }

            if(calculator.getOperation().equalsIgnoreCase("dateMinDays")){
                calculator.setOperation("dateMinDate");
                result.setText(calculator.getDateAsString() + " - " + "dd/mm/yyyy");
            }

            return;
        }

        calculator.createDate(); //Creates a Date object when clicking on Date Button
        calculator.setIsDate(true);
        calculator.setIsTypingDay(true);
        result.setText("dd / mm / yyyy");
    }

    @FXML
    protected void onBinToHexButtonClick(){
        calculator.setOperation("binToHex");
        result.setText("BinToHex: ");
    }

    @FXML
    protected void onHexToBinButtonClick(){
        calculator.setOperation("hexToBin");
        result.setText("HexToBin: ");
    }

    @FXML
    protected void onEqualButtonClick() {

        switch (calculator.getOperation()){
            case "sqrt" -> calculator.setResult(calculator.sqrt(calculator.getNum1()));
            case "sum" -> calculator.setResult(calculator.sum(calculator.getNum1(), calculator.getNum2()));
            case "mul" -> calculator.setResult(calculator.mul(calculator.getNum1(), calculator.getNum2()));
            case "div" -> calculator.setResult(calculator.div(calculator.getNum1(), calculator.getNum2()));
            case "min" -> calculator.setResult(calculator.min(calculator.getNum1(), calculator.getNum2()));
            case "cylinder" -> calculator.setResult(calculator.cylinder(calculator.getNum1(), calculator.getNum2()));
            case "cone" -> calculator.setResult(calculator.cone(calculator.getNum1(), calculator.getNum2()));
            case "factorial" -> calculator.setResult(calculator.factorial((int) calculator.getNum1()));
            case "fibonacci" -> calculator.setResult(calculator.fibonacci((int) calculator.getNum1()));
            case "dateSumDays" -> {
                calculator.setDateResult(calculator.dateSumDays(calculator.getDate(), calculator.getNum1()));
                result.setText("" + calculator.getDateResultAsString());
                return;
            }
            case "dateMinDays" -> {
                calculator.setDateResult(calculator.dateSubtractDays(calculator.getDate(), calculator.getNum1()));
                result.setText("" + calculator.getDateResultAsString());
                return;
            }
            case "dateMinDate" -> {
                calculator.setResult(calculator.dateMinDate(calculator.getDate(), calculator.getDate2()));
                result.setText("" + calculator.getResult());
                return;
            }
            case "binToHex" -> {
                String resultString = calculator.doubleToHexadecimal(calculator.getNum1());
                result.setText(resultString);
                return;
            }
            case "hexToBin" -> {
                String resultString = calculator.hexadecimalToBinary(calculator.getHex());
                result.setText(resultString);
                return;
            }
            default -> System.out.println("nothing");
        }

        result.setText("" + calculator.getResult());
    }

    @FXML
    protected void onClearButtonClick(){
        calculator.reset();
        result.setText("" + calculator.getResult());
    }
}