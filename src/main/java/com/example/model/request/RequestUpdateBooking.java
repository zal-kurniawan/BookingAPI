package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RequestUpdateBooking {
    @JsonProperty("firstname")
    public String firstname;

    @JsonProperty("lastname")
    public String lastname;

    @JsonProperty("totalprice")
    public int totalprice;

    @JsonProperty("depositpaid")
    public boolean depositpaid;

    @JsonProperty("bookingdates")
    public Bookingdates bookingdates;

    @JsonProperty("additionalneeds")
    public String additionalneeds;

    public static class Bookingdates {
        @JsonProperty("checkin")
        public String checkin;

        @JsonProperty("checkout")
        public String checkout;
    }
}
