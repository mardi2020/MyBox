package com.mardi2020.MyBox.user;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {

    public boolean checkValidEmail(String email) {
        String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regx);

        Matcher matcher = pattern.matcher(email);
        System.out.println(email +" : "+ matcher.matches()+"\n");

        return matcher.matches();
    }
}
