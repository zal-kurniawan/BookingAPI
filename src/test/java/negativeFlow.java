import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class negativeFlow {
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
                Assert.assertEquals(response.statusCode(), 200, "Status code should be 200");
                Assert.assertNull(response.jsonPath().getString("token"), "Token should be null");
        }

        @Test
        public void bookingIdDoesNotExist() {
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/99999")
                                .header("Content-Type", "application/json")
                                .when()
                                .get();
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
                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking")
                                .header("Content-Type", "application/json")
                                .body(reqBody)
                                .when()
                                .post();
                Assert.assertEquals(response.statusCode(), 400, "Invalid Request");
        }

        @Test
        public void updateBookingWithoutAuth() {
                String reqBody = "{\n" + //
                                "    \"firstname\" : \"Budi\",\n" + //
                                "    \"lastname\" : \"Brown\",\n" + //
                                "    \"totalprice\" : 111,\n" + //
                                "    \"depositpaid\" : true,\n" + //
                                "    \"bookingdates\" : {\n" + //
                                "        \"checkin\" : \"2018-01-01\",\n" + //
                                "        \"checkout\" : \"2019-01-01\"\n" + //
                                "    },\n" + //
                                "    \"additionalneeds\" : \"Breakfast\"\n" + //
                                "}";

                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/1")
                                .header("Content-Type", "application/json")
                                .body(reqBody)
                                .when()
                                .put();
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }

        @Test
        public void updateBookingWithInvalidAuth() {
                String reqBody = "{\n" + //
                                "    \"firstname\" : \"Budi\",\n" + //
                                "    \"lastname\" : \"Brown\",\n" + //
                                "    \"totalprice\" : 111,\n" + //
                                "    \"depositpaid\" : true,\n" + //
                                "    \"bookingdates\" : {\n" + //
                                "        \"checkin\" : \"2018-01-01\",\n" + //
                                "        \"checkout\" : \"2019-01-01\"\n" + //
                                "    },\n" + //
                                "    \"additionalneeds\" : \"Breakfast\"\n" + //
                                "}";

                Response response = given()
                                .baseUri("https://restful-booker.herokuapp.com/booking/1")
                                .header("Content-Type", "application/json")
                                .header("Cookie", "token=invalid")
                                .body(reqBody)
                                .when()
                                .put();
                Assert.assertEquals(response.statusCode(), 403, "Forbidden");
        }
}
