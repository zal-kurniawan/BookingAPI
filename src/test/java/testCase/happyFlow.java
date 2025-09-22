package testCase;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.example.base.BaseTest;
import apiengine.BookingCollectionAPI;
import io.restassured.response.Response;

public class happyFlow extends BaseTest {
        String bookingId, firstName, lastName, checkIn, checkOut;
        int totalPrice;

        @Test(priority = 1)
        public void createBooking() {
                firstName = "Budi";
                lastName = "Idub";
                totalPrice = 1500000;
                checkIn = "2025-09-16";
                checkOut = "2025-09-17";
                String reqBody = "{\n" + //
                                "    \"firstname\" : \"" + firstName + "\",\n" + //
                                "    \"lastname\" : \"" + lastName + "\",\n" + //
                                "    \"totalprice\" : " + totalPrice + ",\n" + //
                                "    \"depositpaid\" : true,\n" + //
                                "    \"bookingdates\" : {\n" + //
                                "        \"checkin\" : \"" + checkIn + "\",\n" + //
                                "        \"checkout\" : \"" + checkOut + "\"\n" + //
                                "    },\n" + //
                                "    \"additionalneeds\" : \"Dinner\"\n" + //
                                "}";
                Response response = BookingCollectionAPI.createBooking(reqBody);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertNotNull(response.jsonPath().getString("bookingid"), "Booking ID should not be null");
                bookingId = response.jsonPath().getString("bookingid");
        }

        @Test(priority = 2, dependsOnMethods = "createBooking")
        public void assertBookingAfterCreate() {
                Response response = BookingCollectionAPI.getBookingById(bookingId);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(response.jsonPath().getString("firstname"), firstName);
                Assert.assertEquals(response.jsonPath().getString("lastname"), lastName);
                Assert.assertEquals(response.jsonPath().getInt("totalprice"), totalPrice);
                Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"),
                                checkIn);
                Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"),
                                checkOut);

        }

        @Test(priority = 3, dependsOnMethods = "createBooking")
        public void updateBooking() {
                firstName = "Joko";
                lastName = "Fernando";
                totalPrice = 2000000;
                checkIn = "2025-09-20";
                checkOut = "2025-09-21";
                String reqBody = "{\n" + //
                                " \"firstname\" : \"" + firstName + "\",\n" + //
                                " \"lastname\" : \"" + lastName + "\",\n" + //
                                " \"totalprice\" : " + totalPrice + ",\n" + //
                                " \"depositpaid\" : true,\n" + //
                                " \"bookingdates\" : {\n" + //
                                " \"checkin\" : \"" + checkIn + "\",\n" + //
                                " \"checkout\" : \"" + checkOut + "\"\n" + //
                                " },\n" + //
                                " \"additionalneeds\" : \"Dinner\"\n" + //
                                "}";

                Response response = BookingCollectionAPI.updateBooking(bookingId, reqBody, token);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
        }

        @Test(priority = 4, dependsOnMethods = "updateBooking")
        public void assertBookingAfterUpdate() {
                Response response = BookingCollectionAPI.getBookingById(bookingId);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(response.jsonPath().getString("firstname"), firstName);
                Assert.assertEquals(response.jsonPath().getString("lastname"), lastName);
                Assert.assertEquals(response.jsonPath().getInt("totalprice"), totalPrice);
                Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"),
                                checkIn);
                Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"),
                                checkOut);
        }

        @Test(priority = 5, dependsOnMethods = "updateBooking")
        public void updatePartialBooking() {
                firstName = "Jojon";
                lastName = "Sudarsono";
                String reqBody = "{\n" + //
                                "    \"firstname\" : \"" + firstName + "\",\n" + //
                                "    \"lastname\" : \"" + lastName + "\"\n" + //
                                "}";
                Response response = BookingCollectionAPI.partialUpdateBooking(bookingId, reqBody, token);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
        }

        @Test(priority = 6, dependsOnMethods = "updatePartialBooking")
        public void assertBookingAfterPartialUpdate() {
                Response response = BookingCollectionAPI.getBookingById(bookingId);
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertEquals(response.jsonPath().getString("firstname"), firstName);
                Assert.assertEquals(response.jsonPath().getString("lastname"), lastName);
        }

        @Test(priority = 7, dependsOnMethods = "updatePartialBooking")
        public void deleteBooking() {
                Response response = BookingCollectionAPI.deleteBooking(bookingId, token);
                Assert.assertEquals(response.statusCode(), 201, "Status code should be 201");
        }

        @Test(priority = 8, dependsOnMethods = "deleteBooking")
        public void assertBookingAfterDelete() {
                Response response = BookingCollectionAPI.getBookingById(bookingId);
                Assert.assertEquals(response.statusCode(), 404, "Status code should be 404");
        }
}
