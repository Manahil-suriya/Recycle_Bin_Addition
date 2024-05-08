package com.example.contact_app;

import android.app.Application;

import com.example.contact_app.Contact;

import java.util.ArrayList;

public class MyApplication extends Application {
    public static ArrayList<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();
        contacts = new ArrayList<>();
    }
}
