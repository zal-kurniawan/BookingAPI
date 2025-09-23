package testCase;

import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.example.base.BaseTest;
import com.example.model.request.RequestUpdateBooking;
import com.example.utils.Helper;

import apiEngine.BookingAPI;
import io.restassured.response.Response;

public class negativeFlow extends BaseTest {
        @Test
        public void authWithInvalidCredentials() {
                String reqBody = "{\n" + //
                                "    \"username\" : \"admin\",\n" + //
                                "    \"password\" : \"admin\"\n" + //
                                "}";

                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/auth")
                                .header("Content-Type", "application/json")
                                .body(reqBody)
                                .when()
                                .post();
                // Assertion
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertNull(response.jsonPath().getString("token"), "Token should be null");
        }

        @Test
        public void bookingIdDoesNotExist() {
                Response response = BookingAPI.getBookingById("9999999");
                Assert.assertEquals(response.statusCode(), 404, "non-existent booking ID");
        }

        @Test
        public void createBookingWithInvalidData() {
                String reqBody = "{\n" + //
                                "    \"firstname\" : \"Jim\",\n" + //
                                "    \"lastname\" : \"Brown\",\n" + //
                                "    \"totalprice\" : 111,\n" + //
                                "    \"depositpaid\" : true,\n" + //
                                "    \"bookingdates\" : {\n" + //
                                "        \"checkin\" : 2018-01-01,\n" + //
                                "        \"checkout\" : 2019-01-01\n" + //
                                "    },\n" + //
                                "    \"additionalneeds\" : \"Breakfast\"\n" + //
                                "}";
                Response response = BookingAPI.createBooking(reqBody);
                // Assertion
                Assert.assertEquals(response.statusCode(), 400, "Bad Request");
        }

        @Test
        public void updateBookingWithoutAuth() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json", "updateWithoutAuth",
                                RequestUpdateBooking.class);
                // Assertion
                Response response = BookingAPI.updateBooking("1", request, "");
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }

        @Test
        public void updateBookingWithInvalidAuth() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json",
                                "updateWithInvalidAuth",
                                RequestUpdateBooking.class);
                // Assertion
                Response response = BookingAPI.updateBooking("1", request, "invalidtoken");
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }
}
