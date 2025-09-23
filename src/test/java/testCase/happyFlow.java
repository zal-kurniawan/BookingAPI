package testCase;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.example.base.BaseTest;
import com.example.model.request.RequestCreateBooking;
import com.example.model.request.RequestUpdateBooking;
import com.example.model.response.ResponseCreateBooking;
import com.example.model.response.ResponseGetBookingByID;
import com.example.model.response.ResponseUpdateBooking;
import com.example.utils.Helper;
import apiEngine.BookingAPI;
import io.restassured.response.Response;

public class happyFlow extends BaseTest {
        String bookingId, firstName, lastName, checkIn, checkOut;
        int totalPrice;

        @Test(priority = 1)
        public void createBooking() {
                RequestCreateBooking request = Helper.findPayloadByUseCase("create_booking.json", "createBooking1",
                                RequestCreateBooking.class);
                ResponseCreateBooking responseExpected = Helper.findResponseByUseCase("create_booking.json",
                                "createBooking1", ResponseCreateBooking.class);
                Response responseActual = BookingAPI.createBooking(request);
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertNotNull(responseActual.jsonPath().getString("bookingid"), "Booking ID should not be null");
                bookingId = responseActual.jsonPath().getString("bookingid");
                Assert.assertEquals(responseActual.jsonPath().getString("booking.firstname"),
                                responseExpected.booking.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("booking.lastname"),
                                responseExpected.booking.lastname);
                Assert.assertEquals(responseActual.jsonPath().getInt("booking.totalprice"),
                                responseExpected.booking.totalprice);
                Assert.assertEquals(responseActual.jsonPath().getBoolean("booking.depositpaid"),
                                responseExpected.booking.depositpaid);
                Assert.assertEquals(responseActual.jsonPath().getString("booking.bookingdates.checkin"),
                                responseExpected.booking.bookingdates.checkin);
                Assert.assertEquals(responseActual.jsonPath().getString("booking.bookingdates.checkout"),
                                responseExpected.booking.bookingdates.checkout);
                Assert.assertEquals(responseActual.jsonPath().getString("booking.additionalneeds"),
                                responseExpected.booking.additionalneeds);
        }

        @Test(priority = 2, dependsOnMethods = "createBooking")
        public void assertBookingAfterCreate() {
                Response responseActual = BookingAPI.getBookingById(bookingId);
                ResponseGetBookingByID responseExpected = Helper.findResponseByUseCase("get_booking_by_id.json",
                                "getBookingAfterCreate", ResponseGetBookingByID.class);
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(responseActual.jsonPath().getString("firstname"),
                                responseExpected.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("lastname"),
                                responseExpected.lastname);
                Assert.assertEquals(responseActual.jsonPath().getInt("totalprice"), responseExpected.totalprice);
                Assert.assertEquals(responseActual.jsonPath().getBoolean("depositpaid"), responseExpected.depositpaid);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkin"),
                                responseExpected.bookingdates.checkin);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkout"),
                                responseExpected.bookingdates.checkout);
                Assert.assertEquals(responseActual.jsonPath().getString("additionalneeds"),
                                responseExpected.additionalneeds);

        }

        @Test(priority = 3, dependsOnMethods = "createBooking")
        public void updateBooking() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json", "updateBooking1",
                                RequestUpdateBooking.class);
                ResponseUpdateBooking responseExpected = Helper.findResponseByUseCase("update_booking.json",
                                "updateBooking1", ResponseUpdateBooking.class);
                Response responseActual = BookingAPI.updateBooking(bookingId, request,
                                token);
                // Assertion
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(responseActual.jsonPath().getString("firstname"),
                                responseExpected.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("lastname"),
                                responseExpected.lastname);
                Assert.assertEquals(responseActual.jsonPath().getInt("totalprice"), responseExpected.totalprice);
                Assert.assertEquals(responseActual.jsonPath().getBoolean("depositpaid"), responseExpected.depositpaid);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkin"),
                                responseExpected.bookingdates.checkin);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkout"),
                                responseExpected.bookingdates.checkout);
                Assert.assertEquals(responseActual.jsonPath().getString("additionalneeds"),
                                responseExpected.additionalneeds);
        }

        @Test(priority = 4, dependsOnMethods = "updateBooking")
        public void assertBookingAfterUpdate() {
                Response responseActual = BookingAPI.getBookingById(bookingId);
                ResponseGetBookingByID responseExpected = Helper.findResponseByUseCase("get_booking_by_id.json",
                                "getBookingAfterUpdate", ResponseGetBookingByID.class);
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(responseActual.jsonPath().getString("firstname"),
                                responseExpected.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("lastname"),
                                responseExpected.lastname);
                Assert.assertEquals(responseActual.jsonPath().getInt("totalprice"), responseExpected.totalprice);
                Assert.assertEquals(responseActual.jsonPath().getBoolean("depositpaid"), responseExpected.depositpaid);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkin"),
                                responseExpected.bookingdates.checkin);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkout"),
                                responseExpected.bookingdates.checkout);
                Assert.assertEquals(responseActual.jsonPath().getString("additionalneeds"),
                                responseExpected.additionalneeds);
        }

        @Test(priority = 5, dependsOnMethods = "updateBooking")
        public void updatePartialBooking() {
                RequestUpdateBooking request = Helper.findPayloadByUseCase("update_booking.json",
                                "updatePartialBooking1",
                                RequestUpdateBooking.class);
                ResponseUpdateBooking responseExpected = Helper.findResponseByUseCase("update_booking.json",
                                "updatePartialBooking1", ResponseUpdateBooking.class);
                Response responseActual = BookingAPI.partialUpdateBooking(bookingId,
                                request, token);
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(responseActual.jsonPath().getString("firstname"),
                                responseExpected.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("lastname"),
                                responseExpected.lastname);
                Assert.assertEquals(responseActual.jsonPath().getString("additionalneeds"),
                                responseExpected.additionalneeds);
        }

        @Test(priority = 6, dependsOnMethods = "updatePartialBooking")
        public void assertBookingAfterPartialUpdate() {
                Response responseActual = BookingAPI.getBookingById(bookingId);
                ResponseGetBookingByID responseExpected = Helper.findResponseByUseCase("get_booking_by_id.json",
                                "getBookingAfterPartialUpdate", ResponseGetBookingByID.class);
                Assert.assertEquals(responseActual.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(responseActual.jsonPath().getString("firstname"),
                                responseExpected.firstname);
                Assert.assertEquals(responseActual.jsonPath().getString("lastname"),
                                responseExpected.lastname);
                Assert.assertEquals(responseActual.jsonPath().getInt("totalprice"), responseExpected.totalprice);
                Assert.assertEquals(responseActual.jsonPath().getBoolean("depositpaid"), responseExpected.depositpaid);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkin"),
                                responseExpected.bookingdates.checkin);
                Assert.assertEquals(responseActual.jsonPath().getString("bookingdates.checkout"),
                                responseExpected.bookingdates.checkout);
                Assert.assertEquals(responseActual.jsonPath().getString("additionalneeds"),
                                responseExpected.additionalneeds);
        }

        @Test(priority = 7, dependsOnMethods = "updatePartialBooking")
        public void deleteBooking() {
                Response response = BookingAPI.deleteBooking(bookingId, token);
                Assert.assertEquals(response.statusCode(), 201, "Status code should be 201");
        }

        @Test(priority = 8, dependsOnMethods = "deleteBooking")
        public void assertBookingAfterDelete() {
                Response response = BookingAPI.getBookingById(bookingId);
                Assert.assertEquals(response.statusCode(), 404, "Status code should be 404");
        }
}
