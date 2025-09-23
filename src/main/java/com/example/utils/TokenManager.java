package com.example.utils;

import static io.restassured.RestAssured.given;

import com.example.model.request.RequestAuth;

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
        RequestAuth request = Helper.findPayloadByUseCase("authorization.json", "validAuth", RequestAuth.class);
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com/auth")
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post();
        token = response.jsonPath().getString("token");
        return token;
    }
}
