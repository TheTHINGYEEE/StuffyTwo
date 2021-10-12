package com.github.thethingyee.StuffyTwo.validators;

import java.io.IOException;
import java.net.URL;

public class URLValidate {

    public static boolean validate(String s) {
        try {
            new URL(s).openConnection();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}
