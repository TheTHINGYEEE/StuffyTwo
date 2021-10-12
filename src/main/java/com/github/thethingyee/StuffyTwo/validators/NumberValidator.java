package com.github.thethingyee.StuffyTwo.validators;

public class NumberValidator {

    public static boolean validate(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
