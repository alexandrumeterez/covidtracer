package com.example.covidtracer.views;

import android.util.Log;

import com.example.covidtracer.models.User;

public class UserView {
    public static void printUser(User user) {
        System.out.println(user.familyName);
        System.out.println(user.surname);
        System.out.println(user.email);
        System.out.println(user.phone);
    }
}
