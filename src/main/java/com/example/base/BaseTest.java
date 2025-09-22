package com.example.base;

import static io.restassured.RestAssured.given;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import io.restassured.RestAssured;
import com.example.utils.Helper;
import com.example.utils.TokenManager;

public class BaseTest {
    public static String token, baseURI;

    @BeforeSuite
    public void beforeSuite() {
        baseURI = Helper.getKey("BASE_URI");
        token = TokenManager.getToken();
    }

    @BeforeMethod
    public void setupRequestSpecification() {
        RestAssured.requestSpecification = given()
                .baseUri(baseURI)
                .header("Content-Type", "application/json");
    }

    @AfterMethod
    public void afterMethod() {
        if (RestAssured.requestSpecification != null) {
            RestAssured.requestSpecification = null;
        }
    }
}
