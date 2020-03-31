package com.example.covidtracer.views;

import android.util.Log;

import com.example.covidtracer.models.User;

public class UserView {
    public static void printUser(User user) {
        System.out.println(user.phone);
        System.out.println(user.status);
    }
}
