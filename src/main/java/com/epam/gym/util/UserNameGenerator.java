package com.epam.gym.util;

public class UserNameGenerator {

    public static String generateUserName(String firstName, String lastName){
       return new StringBuilder(firstName).append(".").append(lastName).toString();
    }
}
