package apiengine;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class BookingCollectionAPI {

    public static Response getBookingById(String bookingId) {
        return given()
                .when()
                .get("booking/" + bookingId);
    }

    public static Response createBooking(String reqBody) {
        return given()
                .body(reqBody)
                .when()
                .post("booking");
    }

    public static Response updateBooking(String bookingId, String reqBody, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .body(reqBody)
                .when()
                .put("booking/" + bookingId);
    }

    public static Response partialUpdateBooking(String bookingId, String reqBody, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .body(reqBody)
                .when()
                .patch("booking/" + bookingId);
    }

    public static Response deleteBooking(String bookingId, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("booking/" + bookingId);
    }
}