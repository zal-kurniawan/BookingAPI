package testCase;

import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.example.base.BaseTest;
import com.example.model.request.RequestAuth;
import com.example.model.request.RequestUpdateBooking;
import com.example.utils.Helper;

import apiEngine.BookingAPI;
import io.restassured.response.Response;

public class negativeFlow extends BaseTest {
        @Test
        public void authWithInvalidCredentials() {
                RequestAuth request = Helper.findPayloadByUseCase("authorization.json", "invalidAuth",
                                RequestAuth.class);
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/auth")
                                .header("Content-Type", "application/json")
                                .body(request)
                                .when()
                                .post();
                // Assertion
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertNull(response.jsonPath().getString("token"), "Token should be null");
        }

        @Test
        public void bookingIdDoesNotExist() {
                Response response = BookingAPI.getBookingById("9999999");
                // Assertion
                Assert.assertEquals(response.statusCode(), 404, "non-existent booking ID");
        }

        @Test
        public void updateBookingWithoutAuth() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json", "updateWithoutAuth",
                                RequestUpdateBooking.class);
                Response response = BookingAPI.updateBooking("1", request, "");
                // Assertion
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }

        @Test
        public void updateBookingWithInvalidAuth() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json",
                                "updateWithInvalidAuth",
                                RequestUpdateBooking.class);

                Response response = BookingAPI.updateBooking("1", request, "invalidtoken");
                // Assertion
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }

        @Test
        public void deleteBookingWithoutAuth() {
                Response response = BookingAPI.deleteBooking("1", "");
                // Assertion
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }

        @Test
        public void deleteBookingWithInvalidAuth() {
                Response response = BookingAPI.deleteBooking("1", "invalidtoken");
                // Assertion
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }
}
