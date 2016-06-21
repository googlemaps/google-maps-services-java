Java Client for Google Maps Services
====================================

![Build Status](https://travis-ci.org/googlemaps/google-maps-services-java.svg)&nbsp;![Maven Central Version](http://img.shields.io/maven-central/v/com.google.maps/google-maps-services.svg)&nbsp;[![Coverage Status](https://img.shields.io/coveralls/googlemaps/google-maps-services-java.svg)](https://coveralls.io/r/googlemaps/google-maps-services-java)

## Description

Use Java? Want to [geocode][Geocoding API] something? Looking for [directions][Directions API]?
Maybe [matrices of directions][Distance Matrix API]? This library brings the [Google Maps API Web
Services] to your server-side Java application.
![Analytics](https://maps-ga-beacon.appspot.com/UA-12846745-20/google-maps-services-java/readme?pixel)

The Java Client for Google Maps Services is a Java Client library for the following Google Maps
APIs:

 - [Directions API]
 - [Distance Matrix API]
 - [Elevation API]
 - [Geocoding API]
 - [Places API]
 - [Roads API]
 - [Time Zone API]

Keep in mind that the same [terms and conditions](https://developers.google.com/maps/terms) apply
to usage of the APIs when they're accessed through this library.

**Note:** The Java Client for Google Maps Services is for use in server applications. If you're building a
mobile application, you will need to introduce a proxy server to act as intermediary between your mobile
application and the [Google Maps API Web Services]. The Java Client for Google Maps Services would make an
excellent choice as the basis for such a proxy server.

## Support

This library is community supported. We're comfortable enough with the stability and features of
the library that we want you to build real production applications on it. We will try to support,
through Stack Overflow, the public and protected surface of the library and maintain backwards
compatibility in the future; however, while the library is in version 0.x, we reserve the right
to make backwards-incompatible changes. If we do remove some functionality (typically because
better functionality exists or if the feature proved infeasible), our intention is to deprecate
and give developers a year to update their code.

If you find a bug, or have a feature suggestion, please [log an issue][issues]. If you'd like to
contribute, please read [How to Contribute][contrib].

## Requirements

 - Java 1.7 or later.
 - A Google Maps API key.

### API keys

Each Google Maps Web Service requires an API key or Client ID. API keys are
freely available with a Google Account at
https://developers.google.com/console. To generate a server key for
your project:

 1. Visit https://developers.google.com/console and log in with
    a Google Account.
 1. Select an existing project, or create a new project.
 1. Click **Enable an API**.
 1. Browse for the API, and set its status to "On". The Java Client for Google Maps Services
    accesses the following APIs:
    * Directions API
    * Distance Matrix API
    * Elevation API
    * Geocoding API
    * Places API
    * Roads API
    * Time Zone API
 1. Once you've enabled the APIs, click **Credentials** from the left navigation of the Developer
    Console.
 1. In the "Public API access", click **Create new Key**.
 1. Choose **Server Key**.
 1. If you'd like to restrict requests to a specific IP address, do so now.
 1. Click **Create**.

Your API key should be 40 characters long, and begin with `AIza`.

**Important:** This key should be kept secret on your server.

## Installation

You can add the library to your project via Maven or Gradle.

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

You can find the latest version at the top of this README or by searching
[Maven Central](https://search.maven.org/) or [Gradle, Please](http://gradleplease.appspot.com/).

## Developer Documentation

View the [javadoc](https://googlemaps.github.io/google-maps-services-java/v0.1.15/javadoc).

Additional documentation for the included web services is available at
https://developers.google.com/maps/.

 - [Directions API]
 - [Distance Matrix API]
 - [Elevation API]
 - [Geocoding API]
 - [Places API]
 - [Roads API]
 - [Time Zone API]

## Usage

This example uses the [Geocoding API].

```java
GeoApiContext context = new GeoApiContext().setApiKey("AIza...");
GeocodingResult[] results =  GeocodingApi.geocode(context,
    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
System.out.println(results[0].formattedAddress);
```

For more usage examples, check out [the tests](src/test/java/com/google/maps/).

## Features

### Google App Engine Support

You can use this client library on Google App Engine with a single line code change.

```java
GeoApiContext context = new GeoApiContext(new GaeRequestHandler()).setApiKey(API_KEY);
```

The `new GaeRequestHandler()` argument to the `GeoApiContext` constructor tells the
Java Client for Google Maps Services to utilise the apropriate calls for making HTTP
requests from Google App Engine, instead of the default OkHttp based strategy.

### Rate Limiting

Never sleep between requests again! By default, requests are sent at the expected rate limits for
each web service, typically 10 queries per second for free users. If you want to speed up or slow
down requests, you can do that too, using `new GeoApiContext().setQueryRateLimit(qps)`.

### Retry on Failure

Automatically retry when intermittent failures occur. That is, when any of the retriable 5xx errors
are returned from the API.

### Keys *and* Client IDs

Maps API for Work customers can use their [client ID and secret][clientid] to authenticate. Free
customers can use their [API key][apikey], too.

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

## Building the Project

**Note:** You will need an API key or Client ID to run the tests.

    # Compile and package the project
    $ ./gradlew jar

    # Run the tests
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

[apikey]: https://developers.google.com/maps/faq#keysystem
[clientid]: https://developers.google.com/maps/documentation/business/webservices/auth
[contrib]: https://github.com/googlemaps/google-maps-services-java/blob/master/CONTRIB.md
[Directions API]: https://developers.google.com/maps/documentation/directions
[Distance Matrix API]: https://developers.google.com/maps/documentation/distancematrix
[Elevation API]: https://developers.google.com/maps/documentation/elevation
[Geocoding API]: https://developers.google.com/maps/documentation/geocoding
[Google Maps API Web Services]: https://developers.google.com/maps/documentation/webservices/
[issues]: https://github.com/googlemaps/google-maps-services-java/issues
[Places API]: https://developers.google.com/places/web-service/
[Time Zone API]: https://developers.google.com/maps/documentation/timezone
[Roads API]: https://developers.google.com/maps/documentation/roads
