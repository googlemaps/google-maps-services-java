Java Client for Google Maps Services
====================================

Use Java? Want to [geocode](https://developers.google.com/maps/documentation/geocoding) something? Looking for [directions](https://developers.google.com/maps/documentation/directions)? Maybe [matrixes of directions](https://developers.google.com/maps/documentation/distancematrix)? This library brings the [Google Maps API Web Services](https://developers.google.com/maps/documentation/webservices/) to your Java application.
![Analytics](https://ga-beacon.appspot.com/UA-12846745-20/mapsengine-api-java-wrapper/readme?pixel)

Usage
-----

This example uses the [Geocoding API](https://developers.google.com/maps/documentation/geocoding).

```java
GeoApiContext context = new GeoApiContext().setApiKey("AIza...");
GeocodingResult[] results =  GeocodingApi.geocode(context,
    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
System.out.println(results[0].formattedAddress);
```

For more usage examples, check out [the tests](src/test/java/com/google/maps/).

Installation
------------

Add the library to your project via Maven or Gradle.

### Maven
```xml
<dependency>
    <groupId>com.google.maps</groupId>
    <artifactId>google-maps-services</artifactId>
    <version>(insert latest version)</version>
</dependency>
```

### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.google.maps:google-maps-services:(insert latest version)'
    ...
}
```

You can find the latest version by searching [Maven Central](https://search.maven.org/) or [Gradle, Please](http://gradleplease.appspot.com/).

### Note!
Keep in mind that the same [terms and conditions](https://developers.google.com/maps/terms) apply to usage of the APIs when they're accessed through this library.

Features
--------

### Rate Limiting
Never sleep between requests again! By default requests are sent at a maximum of 10 queries per second to match the restrictions on the API. If you want to speed up or slow down requests, you can do that too, using `new GeoApiContext(qps)`.

### Retry on Failure
Automatically retry when intermittent failures occur. That is, when any of the retriable 5xx errors are returned from the API.

### Keys *and* Client IDs
Business customers can use their [client ID and secret](https://developers.google.com/maps/documentation/business/webservices/auth) to authenticate. Free customers can use their [API key](https://developers.google.com/maps/faq#keysystem), too.

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
