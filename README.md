Java Client for Google Maps Services
====================================

[![Build Status](https://travis-ci.org/googlemaps/google-maps-services-java.svg)](https://travis-ci.org/googlemaps/google-maps-services-java)
[![Coverage Status](https://img.shields.io/coveralls/googlemaps/google-maps-services-java.svg)](https://coveralls.io/r/googlemaps/google-maps-services-java)
[![Maven Central Version](http://img.shields.io/maven-central/v/com.google.maps/google-maps-services.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.google.maps%22%20a%3A%22google-maps-services%22)
[![Javadocs](https://www.javadoc.io/badge/com.google.maps/google-maps-services.svg)](https://www.javadoc.io/doc/com.google.maps/google-maps-services)
![GitHub contributors](https://img.shields.io/github/contributors/googlemaps/google-maps-services-java?color=green)
[![Stack Exchange questions](https://img.shields.io/stackexchange/stackoverflow/t/google-maps?color=orange&label=google-maps&logo=stackoverflow)](https://stackoverflow.com/questions/tagged/google-maps)

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
- [Maps Static API]
- [Places API]
- [Roads API]
- [Time Zone API]

Keep in mind that the same [terms and conditions](https://developers.google.com/maps/terms) apply
to usage of the APIs when they're accessed through this library.

## Requirements

- Java 1.8 or later.
- A Google Maps API key.

## API keys
Each Google Maps Web Service request requires an API key. API keys are generated in the 'Credentials' page of the 'APIs & Services' tab of Google Cloud console.

For even more information on getting started with Google Maps Platform and generating an API key, see [Get Started with Google Maps Platform](https://developers.google.com/maps/gmp-get-started) in our docs.

### API Key Security

The Java Client for Google Maps Services is designed for use in both server and Android applications.
In either case, it is important to add [API key restrictions](https://developers.google.com/maps/api-security-best-practices?hl=it)
to improve the security of your API key. Additional security measures, such as hiding your key
from version control, should also be put in place to further improve the security of your API key.

You can refer to [API Security Best Practices](https://developers.google.com/maps/api-security-best-practices) to learn
more about this topic.

**NOTE**: If you are using this library on Android, ensure that your application
is using at least version 0.19.0 of this library so that API key restrictions can be enforced.

## Installation

You can add the library to your project via Maven or Gradle.

**Note:** Since 0.1.18 there is now a dependency on [SLF4J](https://www.slf4j.org/). You need to add
one of the adapter dependencies that makes sense for your logging setup. In the configuration
samples below we are integrating
[slf4j-nop](https://search.maven.org/#artifactdetails%7Corg.slf4j%7Cslf4j-nop%7C1.7.25%7Cjar),
but there are others like
[slf4j-log4j12](https://search.maven.org/#artifactdetails%7Corg.slf4j%7Cslf4j-log4j12%7C1.7.25%7Cjar)
and [slf4j-jdk14](https://search.maven.org/#artifactdetails%7Corg.slf4j%7Cslf4j-jdk14%7C1.7.25%7Cjar)
that will make more sense in other configurations. This will stop a warning message being emitted
when you start using `google-maps-services`.

### Maven

```xml
<dependency>
  <groupId>com.google.maps</groupId>
  <artifactId>google-maps-services</artifactId>
  <version>(insert latest version)</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>1.7.25</version>
</dependency>
```

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.google.maps:google-maps-services:(insert latest version)'
    implementation 'org.slf4j:slf4j-simple:1.7.25'
}
```

You can find the latest version at the top of this README or by [searching
Maven Central](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22google-maps-services%22) or [Gradle, Please](http://gradleplease.appspot.com/#google-maps-services).

## Developer Documentation
View the [javadoc](https://www.javadoc.io/doc/com.google.maps/google-maps-services).

Additional documentation for the included web services is available at
https://developers.google.com/maps/.

- [Directions API]
- [Distance Matrix API]
- [Elevation API]
- [Geocoding API]
- [Maps Static API]
- [Places API]
- [Roads API]
- [Time Zone API]

## Usage

This example uses the [Geocoding API] with an API key:

```java
GeoApiContext context = new GeoApiContext.Builder()
    .apiKey("AIza...")
    .build();
GeocodingResult[] results =  GeocodingApi.geocode(context,
    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
Gson gson = new GsonBuilder().setPrettyPrinting().create();
System.out.println(gson.toJson(results[0].addressComponents));

// Invoke .shutdown() after your application is done making requests
context.shutdown()
```

**Note:** The `GeoApiContext` is designed to be a [Singleton](https://en.wikipedia.org/wiki/Singleton_pattern)
in your application. Please instantiate one on application startup, and continue to use it for the
life of your application. This will enable proper QPS enforcement across all of your requests.

At the end of the execution, call the `shutdown()` method of `GeoApiContext`,
otherwise the thread will remain instantiated in memory.

For more usage examples, check out [the tests](src/test/java/com/google/maps).

## Features

### Google App Engine Support

You can use this client library on Google App Engine with a single code change.

```java
new GeoApiContext.Builder(new GaeRequestHandler.Builder())
    .apiKey("AIza...")
    .build();
```

The `new GaeRequestHandler.Builder()` argument to `GeoApiContext.Builder`'s `requestHandlerBuilder`
tells the Java Client for Google Maps Services to utilise the appropriate calls for making HTTP
requests from Google App Engine, instead of the default [OkHttp3](https://square.github.io/okhttp/)
based strategy.

### Rate Limiting

Never sleep between requests again! By default, requests are sent at the expected rate limits for
each web service, typically 50 queries per second for free users. If you want to speed up or slow
down requests, you can do that too, using `new GeoApiContext.Builder().queryRateLimit(qps).build()`.
Note that you still need to manually handle the [delay between the initial request and successive pages](https://developers.google.com/places/web-service/search#PlaceSearchPaging) when you're paging through multiple result sets.

### Retry on Failure

Automatically retry when intermittent failures occur. That is, when any of the retriable 5xx errors
are returned from the API.

To alter or disable automatic retries, see these methods in `GeoApiContext`:

- `.disableRetries()`
- `.maxRetries()`
- `.retryTimeout()`
- `.setIfExceptionIsAllowedToRetry()`

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
    $ ./gradlew test

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


[apikey]: https://developers.google.com/maps/faq#keysystem
[clientid]: https://developers.google.com/maps/documentation/business/webservices/auth
[contrib]: https://github.com/googlemaps/google-maps-services-java/blob/main/CONTRIB.md
[Directions API]: https://developers.google.com/maps/documentation/directions
[directions-key]: https://developers.google.com/maps/documentation/directions/get-api-key#key
[directions-client-id]: https://developers.google.com/maps/documentation/directions/get-api-key#client-id
[Distance Matrix API]: https://developers.google.com/maps/documentation/distancematrix
[Elevation API]: https://developers.google.com/maps/documentation/elevation
[Geocoding API]: https://developers.google.com/maps/documentation/geocoding
[Google Maps API Web Services]: https://developers.google.com/maps/apis-by-platform#web_service_apis
[issues]: https://github.com/googlemaps/google-maps-services-java/issues
[Maps Static API]: https://developers.google.com/maps/documentation/maps-static/
[Places API]: https://developers.google.com/places/web-service/
[Time Zone API]: https://developers.google.com/maps/documentation/timezone
[Roads API]: https://developers.google.com/maps/documentation/roads
[Making the most of the Google Maps Web Service APIs]: https://maps-apis.googleblog.com/2016/09/making-most-of-google-maps-web-service.html
