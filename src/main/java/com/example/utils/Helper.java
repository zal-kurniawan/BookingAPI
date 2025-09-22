package com.example.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Helper {
    public static Dotenv dotenv;

    public static Dotenv loaDotenv() {
        if (dotenv == null) {
            dotenv = Dotenv.load();
        }
        return dotenv;
    }

    public static String getKey(String key) {
        return loaDotenv().get(key);
    }
}
