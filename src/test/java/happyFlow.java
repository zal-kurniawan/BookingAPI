import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class happyFlow {
        String token, bookingId, firstName, lastName, checkIn, checkOut;
        int totalPrice;

        @BeforeClass
        public void setup() {
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
        }

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
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking")
                                .header("Content-Type", "application/json")
                                .body(reqBody)
                                .when()
                                .post();
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertNotNull(response.jsonPath().getString("bookingid"), "Booking ID should not be null");
                bookingId = response.jsonPath().getString("bookingid");
        }

        @Test(priority = 2, dependsOnMethods = "createBooking")
        public void assertBookingAfterCreate() {
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                                .header("Content-Type", "application/json")
                                .when()
                                .get();
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
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .header("Cookie", "token=" + token)
                                .body(reqBody)
                                .when()
                                .put();
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
        }

        @Test(priority = 4, dependsOnMethods = "updateBooking")
        public void assertBookingAfterUpdate() {
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                                .header("Content-Type", "application/json")
                                .when()
                                .get();
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
        public void deleteBooking() {
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                                .header("Content-Type", "application/json")
                                .header("Cookie", "token=" + token)
                                .when()
                                .delete();
                Assert.assertEquals(response.statusCode(), 201, "Status code should be 201");
        }

        @Test(priority = 6, dependsOnMethods = "deleteBooking")
        public void assertBookingAfterDelete() {
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/" + bookingId)
                                .header("Content-Type", "application/json")
                                .when()
                                .get();
                Assert.assertEquals(response.statusCode(), 404, "Status code should be 404");
        }
}
