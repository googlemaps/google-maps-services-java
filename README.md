Java Client for Google Maps Services
====================================

![Build Status](https://travis-ci.org/googlemaps/google-maps-services-java.svg)&nbsp;![Maven Central Version](http://img.shields.io/maven-central/v/com.google.maps/google-maps-services.svg)&nbsp;[![Coverage Status](https://img.shields.io/coveralls/googlemaps/google-maps-services-java.svg)](https://coveralls.io/r/googlemaps/google-maps-services-java)

Use Java? Want to [geocode](https://developers.google.com/maps/documentation/geocoding) something? Looking for [directions](https://developers.google.com/maps/documentation/directions)? Maybe [matrices of directions](https://developers.google.com/maps/documentation/distancematrix)? This library brings the [Google Maps API Web Services](https://developers.google.com/maps/documentation/webservices/) to your Java application.
![Analytics](https://ga-beacon.appspot.com/UA-12846745-20/google-maps-services-java/readme?pixel)

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

Javadoc
-------

View the
[javadoc](https://googlemaps.github.io/google-maps-services-java/v0.1.2/javadoc).

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

You can find the latest version at the top of this README or by searching [Maven Central](https://search.maven.org/) or [Gradle, Please](http://gradleplease.appspot.com/).

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

### Asynchronous or synchronous -- you choose

All requests support synchronous or asynchronous calling style.
```java
GeocodingApiRequest req = GeocodingApi.newRequest(context).address("Sydney");

// Synchronous
try {
    req.await();
    // Handle successful request.
} catch (Exception e) {
    // Handle error
}

req.awaitIgnoreError(); // No checked exception.

// Async
req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
  @Override
  public void onResult(GeocodingResult[] result) {
    // Handle successful request.
  }

  @Override
  public void onFailure(Throwable e) {
    // Handle error.
  }
});
```

API Coverage
------------

The following APIs are supported:

* [Directions API](https://developers.google.com/maps/documentation/directions)
* [Distance Matrix API](https://developers.google.com/maps/documentation/distancematrix)
* [Elevation API](https://developers.google.com/maps/documentation/elevation)
* [Geocoding API](https://developers.google.com/maps/documentation/geocoding)
* [Time Zone API](https://developers.google.com/maps/documentation/timezone)

Building the project
--------------------

```
# Compile and package the project
$ ./gradlew jar

# Run the tests. Note: you will need an API key to run the tests.
$ API_KEY=AIza.... ./gradlew test

# Run the tests with enterprise credentials.
$ CLIENT_ID=... CLIENT_SECRET=... ./gradlew test

# Generate documentation
$ ./gradlew javadoc

# Publish documentation
$ ./gradlew javadoc
$ git checkout gh-pages
$ rm -rf javadoc
$ mkdir $VERSION
$ mv build/docs/javadoc $VERSION
$ git add $VERSION/javadoc
$ git add latest
$ git commit
$ git push origin gh-pages
```
