package com.mardi2020.MyBox;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class user {


    @Test
    public boolean checkValidEmail() {
        String email = "@hjkl;com";
        String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regx);

        Matcher matcher = pattern.matcher(email);
        System.out.println(email +" : "+ matcher.matches()+"\n");

        return matcher.matches();
    }
}
