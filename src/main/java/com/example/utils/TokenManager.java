package com.example.utils;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class TokenManager {
    public static String token;

    public static String getToken() {
        if (token == null) {
            generateToken();
        }
        return token;
    }

    public static String generateToken() {
        String reqBody = "{\n" + //
                "    \"username\" : \"admin\",\n" + //
                "    \"password\" : \"password123\"\n" + //
                "}";

        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com/auth")
                .header("Content-Type", "application/json")
                .body(reqBody)
                .when()
                .post();
        token = response.jsonPath().getString("token");
        return token;
    }
}
