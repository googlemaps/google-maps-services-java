package com.google.maps.model;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class DirectionsStepTest {

    @Test
    public void testDirectionsStepToString() {
        Distance distance = new Distance();
        distance.humanReadable = "5 miles";
        distance.inMeters = 8046;

        Duration duration = new Duration();
        duration.humanReadable = "10 minutes";
        duration.inSeconds = 600;

        TransitDetails transitDetails = new TransitDetails();
        transitDetails.arrivalStop = new StopDetails();
        transitDetails.arrivalStop.location = new LatLng(37.123, -122.456);
        transitDetails.arrivalStop.name = "Stop A";
        transitDetails.departureStop = new StopDetails();
        transitDetails.departureStop.location = new LatLng(37.456, -122.789);
        transitDetails.departureStop.name = "Stop B";
        transitDetails.arrivalTime = ZonedDateTime.parse("2015-10-01T12:00:00-07:00");
        transitDetails.departureTime = ZonedDateTime.parse("2015-10-01T11:50:00-07:00");

        DirectionsStep step = new DirectionsStep();
        step.htmlInstructions = "Turn left at W. 4th St.";
        step.distance = distance;
        step.duration = duration;
        step.startLocation = new LatLng(37.123, -122.456);
        step.endLocation = new LatLng(37.456, -122.789);
        step.travelMode = TravelMode.DRIVING;
        step.transitDetails = transitDetails;

        String expectedToString = "[DirectionsStep: \"Turn left at W. 4th St.\" (37.12300000,-122.45600000 -> 37.45600000,-122.78900000) driving, duration=10 minutes, distance=5 miles, transitDetails=[Stop B (37.45600000,-122.78900000) at 2015-10-01T11:50-07:00 -> Stop A (37.12300000,-122.45600000) at 2015-10-01T12:00-07:00, 0 stops, headway=0 s]]";
        String actualToString = step.toString();

        assertEquals(expectedToString, actualToString);
    }
}