package com.google.maps.issues;

import org.junit.Test;

public class Issue906 {

    @Test
    public void test() {
        // /maps/api/place/details/json
        // https://maps.googleapis.com/maps/api/place/details/json?key=YOUR_API_KEY&placeid=ChIJTSQM7Smze0gR627zU4Cvkn4
        // The above placeid is expired/not_found sadly and thus cannot be used to test the fix, thus TODO find a place that has the secondary_opening_hours field
        // The example/test json provided in resources also does not contain a single example with this field
    }
}
