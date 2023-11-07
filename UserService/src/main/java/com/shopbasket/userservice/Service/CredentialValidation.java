package com.shopbasket.userservice.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class CredentialValidation {
    private String regex;
    private Pattern pattern;
    private Matcher matcher;
    protected boolean phoneNoValidation(Integer phoneNum){
        regex = "^[0-9]{9}$";
        pattern = Pattern.compile(regex);
        String phoneNo= phoneNum.toString();
        matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }
    protected boolean emailValidation(String email){
        regex = "^[A-Za-z0-9+_.-]+@[A-Za-z]+\\.[A-Za-z]+$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    protected boolean passwordValidation(String password){
        regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
