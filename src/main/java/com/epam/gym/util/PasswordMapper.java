package com.epam.gym.util;

public class PasswordMapper {

    public static char[] toCharArray(String password){
        return password.toCharArray();
    }

    public static String toString(char[] password){
        return new String(password);
    }

}
