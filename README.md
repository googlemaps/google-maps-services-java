Google Maps Web Services Java Client
====================================

![Analytics](https://ga-beacon.appspot.com/UA-12846745-20/mapsengine-api-java-wrapper/readme?pixel)
A native Java library for accessing the [Google Maps API Web Services](https://developers.google.com/maps/documentation/webservices/).


Usage
-----

```java
GeoApiContext context = new GeoApiContext().setApiKey("AIza...");
GeocodingResult[] results =  GeocodingApi.geocode(context,
    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
System.out.println(results[0].formattedAddress);
```

Check out [the tests](src/test/java/com/google/maps/) for more example usage.

Features
--------

### Rate Limiting
Never sleep between requests again! By default requests are sent at a maximum of 10 queries per second to match the restrictions on the API. If you want to speed up or slow down requests, you can do that too, using `new GeoApiContext(qps)`.

### Retry on Failure
Automatically retry when intermittent failures occur. That is, when any of the retriable 5xx errors are returned from the API.

### Keys *and* Client IDs
Business customers can use their [client ID and secret](https://developers.google.com/maps/documentation/business/webservices/auth) to authenticate. Free customers can use their API key, too.

### POJOs
Native objects for each of the API responses.

API Coverage
------------

The following APIs are supported:

* [Directions API](https://developers.google.com/maps/documentation/directions)
* [Distance Matrix API](https://developers.google.com/maps/documentation/distancematrix)
* [Elevation API](https://developers.google.com/maps/documentation/elevation)
* [Geocoding API](https://developers.google.com/maps/documentation/geocoding)
* [Time Zone API](https://developers.google.com/maps/documentation/timezone)
