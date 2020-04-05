package com.hackathon.covidtracer.views;

import com.hackathon.covidtracer.models.User;

public class UserView {
    public static void printUser(User user) {
        System.out.println(user.phone);
        System.out.println(user.status);
    }
}
