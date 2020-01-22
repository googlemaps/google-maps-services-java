# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased](https://github.com/googlemaps/google-maps-services-java/compare/v0.11.0...HEAD)

## [v0.11.0](https://github.com/googlemaps/google-maps-services-java/compare/v0.10.2...v0.11.0) - 2020-01-21

### Bug Fixes

- increase stale bot window ([22a2b4c](https://github.com/googlemaps/google-maps-services-java/commit/22a2b4cb8d6530e59494b9b25681bbae33d5ee60))
- added path exception when there is no center or no zoom (#650) ([47acc92](https://github.com/googlemaps/google-maps-services-java/commit/47acc92)), closes [#650](https://github.com/googlemaps/google-maps-services-java/issues/650)
- added primary school as a poi AddressType to silence SafeEnumAdapter … (#646) ([6500947](https://github.com/googlemaps/google-maps-services-java/commit/6500947)), closes [#646](https://github.com/googlemaps/google-maps-services-java/issues/646)
- typo in FieldMask lon to lng ([#654](https://github.com/googlemaps/google-maps-services-java/issues/654)) ([244d188](https://github.com/googlemaps/google-maps-services-java/commit/244d188a229fdbde29bc397228a2cc1ca28946d6))

### Features

- add support for experience id. ([#647](https://github.com/googlemaps/google-maps-services-java/issues/647)) ([b229806](https://github.com/googlemaps/google-maps-services-java/commit/b229806526c9a1e0b98a71889a209446a1035d36))

## [v0.10.2](https://github.com/googlemaps/google-maps-services-java/compare/v0.10.1...v0.10.2) - 2019-12-10

### Merged

- fix: Add MAX_ROUTE_LENGTH_EXCEEDED Exception [`#632`](https://github.com/googlemaps/google-maps-services-java/pull/632)
- fix: Add more AddressComponentTypes [`#633`](https://github.com/googlemaps/google-maps-services-java/pull/633)
- automate publish to staging repository with additional nexus plugins [`#610`](https://github.com/googlemaps/google-maps-services-java/pull/610)
- mark additional deprecations for alt_id and scope in PlaceDetails [`#613`](https://github.com/googlemaps/google-maps-services-java/pull/613)
- update okhttp dependency [`#614`](https://github.com/googlemaps/google-maps-services-java/pull/614)
- direct users to https://www.javadoc.io/doc/com.google.maps/google-maps-services [`#612`](https://github.com/googlemaps/google-maps-services-java/pull/612)
- add additional place types and deprecate non supported [`#608`](https://github.com/googlemaps/google-maps-services-java/pull/608)
- deprecation warning for place fields: `alt_id`, `id`, `reference`, and `scope` [`#605`](https://github.com/googlemaps/google-maps-services-java/pull/605)

### Commits

- add stale config [`f7b2116`](https://github.com/googlemaps/google-maps-services-java/commit/f7b211626318b6e5ee079a5e211b66720fd3f639)
- update issue templates [`e6273e3`](https://github.com/googlemaps/google-maps-services-java/commit/e6273e39ee33bb4e84fb3c055c170ebed443d298)
- modify stale config [`fd856b8`](https://github.com/googlemaps/google-maps-services-java/commit/fd856b89de01bd0d64b194a2f1c2ad5333b1d778)

## [v0.10.1](https://github.com/googlemaps/google-maps-services-java/compare/v0.10.0...v0.10.1) - 2019-09-23

### Merged

- Fixes issue where deps were not being added to pom.xml [`#606`](https://github.com/googlemaps/google-maps-services-java/pull/606)
- Add tourist_attraction address type [`#601`](https://github.com/googlemaps/google-maps-services-java/pull/601)
- add changelog [`#598`](https://github.com/googlemaps/google-maps-services-java/pull/598)
- add subfields to mask values for place details request [`#597`](https://github.com/googlemaps/google-maps-services-java/pull/597)
- add github issue templates [`#595`](https://github.com/googlemaps/google-maps-services-java/pull/595)
- Include plus_code in PlaceDetailsRequest.FieldMask [`#594`](https://github.com/googlemaps/google-maps-services-java/pull/594)

### Commits

- update changelog for 0.10.1 [`943c0d9`](https://github.com/googlemaps/google-maps-services-java/commit/943c0d972301bb5bbf0980cd0951665eec6a5b30)
- add contributor and stackoverflow badges [`47b5c1c`](https://github.com/googlemaps/google-maps-services-java/commit/47b5c1cfca0ed093a81a3c11e97a6d19adb32892)

## [v0.10.0](https://github.com/googlemaps/google-maps-services-java/compare/v0.9.4...v0.10.0) - 2019-08-27

### Merged

- Updates to build/release and v0.10.0 version rev [`#592`](https://github.com/googlemaps/google-maps-services-java/pull/592)
- Making serializable PlusCode [`#591`](https://github.com/googlemaps/google-maps-services-java/pull/591)
- Upgrades build process to use maven-publish plugin [`#590`](https://github.com/googlemaps/google-maps-services-java/pull/590)
- add userRatingsTotal to PlaceDetails, add unit tests [`#587`](https://github.com/googlemaps/google-maps-services-java/pull/587)
- Add an overloaded method to enable query and location parameters [`#588`](https://github.com/googlemaps/google-maps-services-java/pull/588)

### Commits

- Update JavaDoc [`ec0f0a8`](https://github.com/googlemaps/google-maps-services-java/commit/ec0f0a827f6e61b464c67e54d1a2c752ec05141d)
- Opening up v0.9.5 for development [`68993f0`](https://github.com/googlemaps/google-maps-services-java/commit/68993f053bc9324e3a4dc597eeedaf0a3bfcef7d)

## [v0.9.4](https://github.com/googlemaps/google-maps-services-java/compare/v0.9.3...v0.9.4) - 2019-08-07

### Merged

- Releasing v0.9.4 [`#586`](https://github.com/googlemaps/google-maps-services-java/pull/586)
- Gradle downgrade 5.5.1 -&gt; 5.0 for broken signing [`#585`](https://github.com/googlemaps/google-maps-services-java/pull/585)
- Versions version update [`#584`](https://github.com/googlemaps/google-maps-services-java/pull/584)
- Version update [`#582`](https://github.com/googlemaps/google-maps-services-java/pull/582)
- #572: updated the access modifier for response inner classes in Api Classes [`#581`](https://github.com/googlemaps/google-maps-services-java/pull/581)
- Fix: Format java files that break tests using gradlew check [`#573`](https://github.com/googlemaps/google-maps-services-java/pull/573)
- Add user_ratings_total to the PlaceSearchResult [`#571`](https://github.com/googlemaps/google-maps-services-java/pull/571)
- Add Serializable definition to AutoCompletePrediction$Term class [`#569`](https://github.com/googlemaps/google-maps-services-java/pull/569)
- [NOT URGENT] add new textSearchQuery (overload) method with test [`#567`](https://github.com/googlemaps/google-maps-services-java/pull/567)
- Validate TextSearchRequest without query if type is set [`#566`](https://github.com/googlemaps/google-maps-services-java/pull/566)
- Add implementation of Serializable to AutocompletePrediction [`#565`](https://github.com/googlemaps/google-maps-services-java/pull/565)
- Add Light Rail Station into PlaceType [`#562`](https://github.com/googlemaps/google-maps-services-java/pull/562)
- Add meal_delivery AddressComponentType [`#561`](https://github.com/googlemaps/google-maps-services-java/pull/561)

### Commits

- add new textSearchQuery method with test [`96a6a56`](https://github.com/googlemaps/google-maps-services-java/commit/96a6a561cbae878404fac4c4b6e3413134632c34)
- Add user_ratings_total response field to PlaceSearchResult class [`a8762d8`](https://github.com/googlemaps/google-maps-services-java/commit/a8762d8474fd516a08f84444a4a6ff5e19c2c1d3)
- OKHttp3 v4.0.1 -&gt; v3.14.2  due to warnings [`d8f69b8`](https://github.com/googlemaps/google-maps-services-java/commit/d8f69b89c91dc498fabfaf259085dfae30d92437)

## [v0.9.3](https://github.com/googlemaps/google-maps-services-java/compare/v0.9.2...v0.9.3) - 2019-03-20

### Merged

- Version uplift [`#555`](https://github.com/googlemaps/google-maps-services-java/pull/555)
- Add new address and address component types [`#554`](https://github.com/googlemaps/google-maps-services-java/pull/554)

### Commits

- Releasing version 0.9.3 [`bc26e75`](https://github.com/googlemaps/google-maps-services-java/commit/bc26e75c6232ca3d69f17c2a09e1543d9a23e538)
- Opening up v0.9.3 for development [`027b37d`](https://github.com/googlemaps/google-maps-services-java/commit/027b37d3fcbec30ea8995cf3f5f6704eb9bbb6e9)
- Linking to v0.9.2 javadoc [`c5bc2e7`](https://github.com/googlemaps/google-maps-services-java/commit/c5bc2e74aa49addd8ca47bfe5756b728da656df9)

## [v0.9.2](https://github.com/googlemaps/google-maps-services-java/compare/v0.9.1...v0.9.2) - 2019-02-28

### Merged

- Reverting Gradle version. [`#550`](https://github.com/googlemaps/google-maps-services-java/pull/550)
- ./gradlew depUp [`#549`](https://github.com/googlemaps/google-maps-services-java/pull/549)
- Fixing Google Java Style [`#543`](https://github.com/googlemaps/google-maps-services-java/pull/543)
- adding a default no-arg constructor to EncodedPolyline [`#542`](https://github.com/googlemaps/google-maps-services-java/pull/542)
- TextSearch: add region parameter [`#538`](https://github.com/googlemaps/google-maps-services-java/pull/538)
- Upgrade to commons-lang3 [`#537`](https://github.com/googlemaps/google-maps-services-java/pull/537)
- ./gradlew depUp [`#536`](https://github.com/googlemaps/google-maps-services-java/pull/536)
- ./gradlew googleJavaFormat [`#530`](https://github.com/googlemaps/google-maps-services-java/pull/530)
- Added support to Place IDs [`#526`](https://github.com/googlemaps/google-maps-services-java/pull/526)
- ./gradlew dependencyUpdates [`#529`](https://github.com/googlemaps/google-maps-services-java/pull/529)
-  Looks like case of DateTimeFormat is locale dependant. [`#528`](https://github.com/googlemaps/google-maps-services-java/pull/528)
- Formatting and making the tests pass [`#527`](https://github.com/googlemaps/google-maps-services-java/pull/527)
- Adds a method for setting the departure time as "now" [`#525`](https://github.com/googlemaps/google-maps-services-java/pull/525)
- Fix a small formatting mistake in the docs. [`#523`](https://github.com/googlemaps/google-maps-services-java/pull/523)
- Update README.md [`#522`](https://github.com/googlemaps/google-maps-services-java/pull/522)

### Commits

- Clean up [`6097ff3`](https://github.com/googlemaps/google-maps-services-java/commit/6097ff3f08c9676f84157269a24e208c5d72c89d)
- Added a method to make waypoints from Place IDs [`d2d05a7`](https://github.com/googlemaps/google-maps-services-java/commit/d2d05a7cf124bec97e09a6cd0004d08f95004b25)
- updating google java format to 0.8-SNAPSHOT [`de357b7`](https://github.com/googlemaps/google-maps-services-java/commit/de357b77feaa536b287cfbe8a14b13d87825910d)

## [v0.9.1](https://github.com/googlemaps/google-maps-services-java/compare/v0.9.0...v0.9.1) - 2018-11-28

### Merged

- Force Jacoco version 0.8.2 for OpenJDK 11 compatibility [`#517`](https://github.com/googlemaps/google-maps-services-java/pull/517)
- Fix Google Error Prone warnings [`#513`](https://github.com/googlemaps/google-maps-services-java/pull/513)
- DirectionsLeg, TransitDetails: use ZonedDateTime instead of LocalDateTime [`#516`](https://github.com/googlemaps/google-maps-services-java/pull/516)
- Versions update [`#515`](https://github.com/googlemaps/google-maps-services-java/pull/515)
- Add StaticMapsRequest.path(EncodedPolyline) [`#511`](https://github.com/googlemaps/google-maps-services-java/pull/511)
- Added Maps Static API in README.md [`#507`](https://github.com/googlemaps/google-maps-services-java/pull/507)
- Fixed typo in field [`#506`](https://github.com/googlemaps/google-maps-services-java/pull/506)

### Commits

- Add missing @Override annotations [`7c7a72c`](https://github.com/googlemaps/google-maps-services-java/commit/7c7a72c8f70e2967346eed971d9fd7bda237f1d4)
- Avoid implicit use of default charset [`88204f6`](https://github.com/googlemaps/google-maps-services-java/commit/88204f6406c355b6da2596b63efcf7bbd472391d)
- Updates and making tests pass [`c9f89bc`](https://github.com/googlemaps/google-maps-services-java/commit/c9f89bc525923b334f8a5a6eac428e7dce0775fc)

## [v0.9.0](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.11...v0.9.0) - 2018-09-24

### Merged

- Version 0.9 [`#503`](https://github.com/googlemaps/google-maps-services-java/pull/503)
- Java 1.8 is now the minimum supported version [`#502`](https://github.com/googlemaps/google-maps-services-java/pull/502)
- Dependencies update, reformat, and fixup [`#501`](https://github.com/googlemaps/google-maps-services-java/pull/501)
- Migrate to java.time [`#421`](https://github.com/googlemaps/google-maps-services-java/pull/421)
- SessionToken is now serializable [`#495`](https://github.com/googlemaps/google-maps-services-java/pull/495)

### Commits

- Java 1.8 is the new minimum [`ab013ba`](https://github.com/googlemaps/google-maps-services-java/commit/ab013bac3a4871329f77ca67a37a6d2ce3d2c0e6)
- Tidyup [`839fbd1`](https://github.com/googlemaps/google-maps-services-java/commit/839fbd1c8cfdac75ded5dca678f7ab452d4d5e9a)
- Opening up v0.2.12 development [`5809326`](https://github.com/googlemaps/google-maps-services-java/commit/5809326ee8da2dc60dce9a3116eea71cd59516cd)

## [v0.2.11](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.10...v0.2.11) - 2018-08-31

### Merged

- Updated dependencies [`#494`](https://github.com/googlemaps/google-maps-services-java/pull/494)
- Enabling setting SessionToken for placeDetails [`#493`](https://github.com/googlemaps/google-maps-services-java/pull/493)
- Opening up v0.2.11 for development [`#490`](https://github.com/googlemaps/google-maps-services-java/pull/490)

### Commits

- Release v0.2.11 [`a22237e`](https://github.com/googlemaps/google-maps-services-java/commit/a22237e9fb0c39d3b8e9e7f4c308788a4c8d776c)
- Update README.md [`4fb6af0`](https://github.com/googlemaps/google-maps-services-java/commit/4fb6af021a2019688ed057f1e87adafd36799354)
- Updating link to v0.2.10 Javadoc [`6894d11`](https://github.com/googlemaps/google-maps-services-java/commit/6894d11c2d9f8cb7c19d51b2b5b4925ac395bc2e)

## [v0.2.10](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.9...v0.2.10) - 2018-08-15

### Merged

- Releasing version 0.2.10 [`#489`](https://github.com/googlemaps/google-maps-services-java/pull/489)
- Updating dependencies [`#486`](https://github.com/googlemaps/google-maps-services-java/pull/486)
- Cleaning up [`#485`](https://github.com/googlemaps/google-maps-services-java/pull/485)
- ./gradlew googleJavaFormat [`#484`](https://github.com/googlemaps/google-maps-services-java/pull/484)
- Replace "baseUrlForTesting()" with usage-neutral "baseUrlOverride()" [`#471`](https://github.com/googlemaps/google-maps-services-java/pull/471)
- Tidying up [`#483`](https://github.com/googlemaps/google-maps-services-java/pull/483)
- Fix for #478 [`#482`](https://github.com/googlemaps/google-maps-services-java/pull/482)
- PlaceDetails: add adr_address support [`#480`](https://github.com/googlemaps/google-maps-services-java/pull/480)
- Remove Radar-related test resource files [`#481`](https://github.com/googlemaps/google-maps-services-java/pull/481)
- Remove Radar Search support [`#469`](https://github.com/googlemaps/google-maps-services-java/pull/469)
- ./gradlew googleJavaFormat [`#476`](https://github.com/googlemaps/google-maps-services-java/pull/476)
- Add non-stopover waypoint support [`#468`](https://github.com/googlemaps/google-maps-services-java/pull/468)
- Change *RequestHandler Builder signatures to do chainable calls [`#470`](https://github.com/googlemaps/google-maps-services-java/pull/470)
- Supply -html4 to javadoc with JDK 10 and later [`#472`](https://github.com/googlemaps/google-maps-services-java/pull/472)
- Update gradle dependency keywords [`#474`](https://github.com/googlemaps/google-maps-services-java/pull/474)
- Fix an NPE in DirectionsStep.toString() [`#475`](https://github.com/googlemaps/google-maps-services-java/pull/475)
-  Opening up v0.2.10 for development [`#466`](https://github.com/googlemaps/google-maps-services-java/pull/466)

### Commits

- Remove support for Radar Search, which is now past EOL as of June 30, 2018 [`e814a89`](https://github.com/googlemaps/google-maps-services-java/commit/e814a89befde30059c1b7b058ba3bc7ff2771b1f)
- Add non-stopover Waypoint support. [`e42752f`](https://github.com/googlemaps/google-maps-services-java/commit/e42752f7af776ae33b74640975535d7abce52850)
- Add regression test for DirectionsResult.toString() [`7c60f67`](https://github.com/googlemaps/google-maps-services-java/commit/7c60f67445f8465575945525e89dc124a0743119)

## [v0.2.9](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.8...v0.2.9) - 2018-07-06

### Merged

- Releasing v0.2.9 [`#465`](https://github.com/googlemaps/google-maps-services-java/pull/465)
- Add okHttpClientBuilder() for customizing the OkHttpClient [`#464`](https://github.com/googlemaps/google-maps-services-java/pull/464)
- Add "continent" AddressType and AddressComponentType [`#463`](https://github.com/googlemaps/google-maps-services-java/pull/463)
- Add value-based toString()s for model objects [`#452`](https://github.com/googlemaps/google-maps-services-java/pull/452)
- Add Plus Code support to GeocodingResult and PlaceDetails [`#459`](https://github.com/googlemaps/google-maps-services-java/pull/459)
- Tidying up tests [`#461`](https://github.com/googlemaps/google-maps-services-java/pull/461)
- Reverting SLF4J version bump. [`#460`](https://github.com/googlemaps/google-maps-services-java/pull/460)
- Add note on GeoApiContext reuse in Javadoc [`#456`](https://github.com/googlemaps/google-maps-services-java/pull/456)
- Have OkHttpRequestHandler evict connectionPool on shutdown [`#455`](https://github.com/googlemaps/google-maps-services-java/pull/455)
- Deprecate NearbySearchRequest.type(PlaceType...) [`#454`](https://github.com/googlemaps/google-maps-services-java/pull/454)
- PlaceDetails: make types an AddressType[] instead of String[] [`#453`](https://github.com/googlemaps/google-maps-services-java/pull/453)
- Adjust link in README so Markdown inspectors do not complain [`#451`](https://github.com/googlemaps/google-maps-services-java/pull/451)
- Improve EnumsTest unit test for AddressType and AddressComponentType [`#444`](https://github.com/googlemaps/google-maps-services-java/pull/444)
-  Bringing dependencies up to date. [`#449`](https://github.com/googlemaps/google-maps-services-java/pull/449)
- ./gradlew googleJavaFormat [`#448`](https://github.com/googlemaps/google-maps-services-java/pull/448)
- Replace bogus @url Javadoc tag with HTML link [`#447`](https://github.com/googlemaps/google-maps-services-java/pull/447)
- Minor code hygiene suggestions [`#446`](https://github.com/googlemaps/google-maps-services-java/pull/446)
- Add Javadoc for DirectionsApiRequest static methods [`#443`](https://github.com/googlemaps/google-maps-services-java/pull/443)
- LocalTestServerContext: use "expected[s]" as assertion parameter name [`#445`](https://github.com/googlemaps/google-maps-services-java/pull/445)
- Add more AddressTypes and AddressComponentTypes [`#437`](https://github.com/googlemaps/google-maps-services-java/pull/437)
- New AddressComponentTypes: general_contractor, food, store, etc... [`#436`](https://github.com/googlemaps/google-maps-services-java/pull/436)
- Add missing fields to PlaceDetails.Review. [`#432`](https://github.com/googlemaps/google-maps-services-java/pull/432)

### Fixed

- Reverting SLF4J version bump. [`#457`](https://github.com/googlemaps/google-maps-services-java/issues/457)

### Commits

- Merge upstream master [`eb72951`](https://github.com/googlemaps/google-maps-services-java/commit/eb72951102dc178ede794048459f17834219e71e)
- EnumsTest: improve source code and results readability [`9cb1014`](https://github.com/googlemaps/google-maps-services-java/commit/9cb1014c0b706a60a6f94458176774a189fb53c0)
- EnumsTest: add missing entries for AddressType [`d32d598`](https://github.com/googlemaps/google-maps-services-java/commit/d32d598146cec3034e59966febb40f3dad0d6da9)

## [v0.2.8](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.7...v0.2.8) - 2018-06-25

### Merged

- Find place from text, and place details field masks. [`#424`](https://github.com/googlemaps/google-maps-services-java/pull/424)
- Add new types to AddressType enum. [`#428`](https://github.com/googlemaps/google-maps-services-java/pull/428)
- ./gradlew googleJavaFormat [`#423`](https://github.com/googlemaps/google-maps-services-java/pull/423)
- Add support for scale parameter on custom icons to be able to use hi-DPI icons when map scale &gt; 1 [`#419`](https://github.com/googlemaps/google-maps-services-java/pull/419)

### Commits

- Find place by text, and place details field masks. [`acf74b4`](https://github.com/googlemaps/google-maps-services-java/commit/acf74b404ed81dd993dff1ef5b80be1673d86519)
- Adding LocationBias parameter [`bca82fe`](https://github.com/googlemaps/google-maps-services-java/commit/bca82fe5a8eb544fef629b680df6df1d526c1af8)
- Field masks differ between place details and find place from text. [`3620c27`](https://github.com/googlemaps/google-maps-services-java/commit/3620c27f7c5b9a829106c585abda79c652632aa2)

## [v0.2.7](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.6...v0.2.7) - 2018-04-10

### Merged

- Releasing version 0.2.7 [`#415`](https://github.com/googlemaps/google-maps-services-java/pull/415)
- README update, and googleJavaFormat fix [`#414`](https://github.com/googlemaps/google-maps-services-java/pull/414)
- Adding Static Maps API [`#413`](https://github.com/googlemaps/google-maps-services-java/pull/413)
- Fix for #360 [`#410`](https://github.com/googlemaps/google-maps-services-java/pull/410)
- close response on retry [`#409`](https://github.com/googlemaps/google-maps-services-java/pull/409)
- gradlew googleJavaFormat [`#406`](https://github.com/googlemaps/google-maps-services-java/pull/406)
- Added missing enums fields [`#405`](https://github.com/googlemaps/google-maps-services-java/pull/405)
- added museum to address type [`#404`](https://github.com/googlemaps/google-maps-services-java/pull/404)

### Commits

- Filling in StaticMapRequest, along with an additional AddressType [`2486d00`](https://github.com/googlemaps/google-maps-services-java/commit/2486d003a18a183595d3c6ceb2e509c53a4899d4)
- Initial Static Maps implementation [`593b194`](https://github.com/googlemaps/google-maps-services-java/commit/593b1943151441fa070339e94527eb6e4d3c667c)
- Adding tests [`98ad5b3`](https://github.com/googlemaps/google-maps-services-java/commit/98ad5b3003a0b8bf9dfd966903a8015cfa266974)

## [v0.2.6](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.5...v0.2.6) - 2018-01-09

### Merged

- Releasing version 0.2.6 [`#402`](https://github.com/googlemaps/google-maps-services-java/pull/402)
- Increase default QPS from 10 to 50 [`#401`](https://github.com/googlemaps/google-maps-services-java/pull/401)
- Set ComponentFilter's Constructor Public [`#397`](https://github.com/googlemaps/google-maps-services-java/pull/397)
- Revert "Revert "Made the constructor public"" [`#395`](https://github.com/googlemaps/google-maps-services-java/pull/395)
- Revert "Made the constructor public" [`#394`](https://github.com/googlemaps/google-maps-services-java/pull/394)
- Made the constructor public [`#393`](https://github.com/googlemaps/google-maps-services-java/pull/393)
- Add java.io.Serializable for inner classes, as it was only added in t… [`#389`](https://github.com/googlemaps/google-maps-services-java/pull/389)

### Commits

- Add java.io.Serializable for inner classes, as it was only added in top-level ones. [`887f288`](https://github.com/googlemaps/google-maps-services-java/commit/887f2888b0d0c7e82436e4afcf79f5bcbbc85e5c)
- Made ComponentFilter's Class Constructor Public [`eac7f77`](https://github.com/googlemaps/google-maps-services-java/commit/eac7f77100f3d46d83637bdc7a23720a1fa99b67)
- Made ComponentFilter's Construtor Private [`ca28a78`](https://github.com/googlemaps/google-maps-services-java/commit/ca28a78dd76406bf957a33d5535a9883f7aba900)

## [v0.2.5](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.4...v0.2.5) - 2017-11-15

### Merged

- Version 0.2.5 release [`#387`](https://github.com/googlemaps/google-maps-services-java/pull/387)
-  ./gradlew googleJavaFormat [`#386`](https://github.com/googlemaps/google-maps-services-java/pull/386)
- Adding strictbounds to Places Autocomplete [`#385`](https://github.com/googlemaps/google-maps-services-java/pull/385)
- AddressType: add night_club [`#384`](https://github.com/googlemaps/google-maps-services-java/pull/384)
- AddressType: add travel_agency [`#382`](https://github.com/googlemaps/google-maps-services-java/pull/382)
- AddressType: add shoe_store [`#381`](https://github.com/googlemaps/google-maps-services-java/pull/381)
- AddressType: add more types observed by steveetl [`#380`](https://github.com/googlemaps/google-maps-services-java/pull/380)
- added java.io.Serializable to the response model #366 [`#367`](https://github.com/googlemaps/google-maps-services-java/pull/367)
- AddressType: add beauty care types [`#378`](https://github.com/googlemaps/google-maps-services-java/pull/378)
- AddressType: add STADIUM [`#379`](https://github.com/googlemaps/google-maps-services-java/pull/379)
- AddressType: add CASINO and PARKING [`#377`](https://github.com/googlemaps/google-maps-services-java/pull/377)
- Opening up v0.2.5 development [`#373`](https://github.com/googlemaps/google-maps-services-java/pull/373)
- Update Javadoc link for v0.2.4 [`#374`](https://github.com/googlemaps/google-maps-services-java/pull/374)
- Suppress unchecked warning in GeoApiContextTest [`#372`](https://github.com/googlemaps/google-maps-services-java/pull/372)
- Adding required import, and googleJavaFormat [`#370`](https://github.com/googlemaps/google-maps-services-java/pull/370)
- ResponseBody in OkHttpPendingResult was not being closed. [`#368`](https://github.com/googlemaps/google-maps-services-java/pull/368)
- added a shutdown method to GeoApiContext which stops RateLimitExecutorDelayThread #261 [`#365`](https://github.com/googlemaps/google-maps-services-java/pull/365)

### Commits

- added java.io.Serializable to the response model [`7bce9dd`](https://github.com/googlemaps/google-maps-services-java/commit/7bce9dd3c129d48e21572f41de157abf407ae171)
- remove comment on serialVersionUID [`8157552`](https://github.com/googlemaps/google-maps-services-java/commit/815755229ba54053f425a335abdb436307b1324f)
- ./gradlew googleJavaFormat [`d36f5ff`](https://github.com/googlemaps/google-maps-services-java/commit/d36f5ff5e94ea33e343848a8bc6fcd1c73cdcbb5)

## [v0.2.4](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.3...v0.2.4) - 2017-10-06

### Merged

- Rolling 0.2.4 release [`#358`](https://github.com/googlemaps/google-maps-services-java/pull/358)
- Tidyup warnings [`#357`](https://github.com/googlemaps/google-maps-services-java/pull/357)
- Upgrade Gradle [`#356`](https://github.com/googlemaps/google-maps-services-java/pull/356)
- ./gradlew googleJavaFormat [`#354`](https://github.com/googlemaps/google-maps-services-java/pull/354)
- Replace Guava dep with local RateLimiter implementation copy-paste [`#351`](https://github.com/googlemaps/google-maps-services-java/pull/351)
- .gitignore: add /out/ to support IntelliJ IDEA [`#352`](https://github.com/googlemaps/google-maps-services-java/pull/352)

### Commits

- Copy Guava's RateLimiter implementation locally and remove Guava dependency [`9622a59`](https://github.com/googlemaps/google-maps-services-java/commit/9622a59b0c919fa6334d0966952760d16ada532f)
- Opening up v0.2.4 development [`7785c57`](https://github.com/googlemaps/google-maps-services-java/commit/7785c57c1563d14ff6afb555a0efaed8173785ec)
- Updating Javadoc link for v0.2.3 [`8ca6e15`](https://github.com/googlemaps/google-maps-services-java/commit/8ca6e1550537f85f574ce7eaff7dcb635b685939)

## [v0.2.3](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.2...v0.2.3) - 2017-09-13

### Merged

- Releasing version 0.2.3 [`#346`](https://github.com/googlemaps/google-maps-services-java/pull/346)
- Cleaning up README.md's markdown [`#345`](https://github.com/googlemaps/google-maps-services-java/pull/345)
- ./gradlew googleJavaFormat [`#343`](https://github.com/googlemaps/google-maps-services-java/pull/343)
- adding proxy authentication in GeoApiContext [`#337`](https://github.com/googlemaps/google-maps-services-java/pull/337)
- Add note about paging delay [`#339`](https://github.com/googlemaps/google-maps-services-java/pull/339)
- Documenting GAE usage [`#335`](https://github.com/googlemaps/google-maps-services-java/pull/335)
- Opening up development on v0.2.3 [`#333`](https://github.com/googlemaps/google-maps-services-java/pull/333)

### Commits

- adding proxy authentication in GeoApiContext - googleJavaFormat [`bd5303d`](https://github.com/googlemaps/google-maps-services-java/commit/bd5303d9f8ee1d6694030848bbd45aba77b89e00)
- Documenting new GAE usage [`ddb5363`](https://github.com/googlemaps/google-maps-services-java/commit/ddb536333c515b6d9b909e60e1334587560c5cf6)
- Javadoc for v0.2.2 [`69fd291`](https://github.com/googlemaps/google-maps-services-java/commit/69fd2913acaec29ab8039c97e569777a8828ff6c)

## [v0.2.2](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.1...v0.2.2) - 2017-08-15

### Merged

- Releasing v0.2.2 [`#332`](https://github.com/googlemaps/google-maps-services-java/pull/332)
- Adding handling for AutocompletePrediction's structured formatting. [`#330`](https://github.com/googlemaps/google-maps-services-java/pull/330)
- guava v22 and v23 reintroduced java 7 support [`#331`](https://github.com/googlemaps/google-maps-services-java/pull/331)
- LatLng serialisation constructor. [`#328`](https://github.com/googlemaps/google-maps-services-java/pull/328)
- ./gradlew googleJavaFormat [`#327`](https://github.com/googlemaps/google-maps-services-java/pull/327)
- Javadoc: Rephrase to match Google Java Style Guide (classes P) [`#321`](https://github.com/googlemaps/google-maps-services-java/pull/321)
- Javadoc: Rephrase to match Google Java Style Guide (classes G) [`#319`](https://github.com/googlemaps/google-maps-services-java/pull/319)
- Javadoc: Rephrase to match Google Java Style Guide (classes Q-S) [`#322`](https://github.com/googlemaps/google-maps-services-java/pull/322)
- Javadoc: Rephrase to match Google Java Style Guide (classes T) [`#323`](https://github.com/googlemaps/google-maps-services-java/pull/323)
- Javadoc: Rephrase to match Google Java Style Guide (classes U-Z) [`#324`](https://github.com/googlemaps/google-maps-services-java/pull/324)
- Javadoc: Rephrase to match Google Java Style Guide (classes H-O) [`#320`](https://github.com/googlemaps/google-maps-services-java/pull/320)
- Javadoc: Use "latitude/longitude" instead of "latitude,longitude" everywhere [`#325`](https://github.com/googlemaps/google-maps-services-java/pull/325)
- Use Oxford commas in Javadoc [`#317`](https://github.com/googlemaps/google-maps-services-java/pull/317)
- Javadoc: Rephrase to match Google Java Style Guide (classes D-F) [`#318`](https://github.com/googlemaps/google-maps-services-java/pull/318)
- Dropping truncated JavaDoc [`#316`](https://github.com/googlemaps/google-maps-services-java/pull/316)
- Javadoc: Rephrase to match Google Java Style Guide (classes A-C) [`#315`](https://github.com/googlemaps/google-maps-services-java/pull/315)
- Javadoc: Update links and fix typos [`#314`](https://github.com/googlemaps/google-maps-services-java/pull/314)
- README: Point "javadoc" link at the new v0.2.1 javadocs [`#311`](https://github.com/googlemaps/google-maps-services-java/pull/311)
- Update build.gradle - Changed guava to v20 [`#308`](https://github.com/googlemaps/google-maps-services-java/pull/308)

### Commits

- Cleaning up Javadoc, and fixing class name typo. (Whups) [`7459bd2`](https://github.com/googlemaps/google-maps-services-java/commit/7459bd26441a60e3943f8e7ae8ddd74241afda5c)
- Use Oxford commas everywhere in Javadoc [`f12ce8a`](https://github.com/googlemaps/google-maps-services-java/commit/f12ce8a4057bcef0e8beb2653ccae712caa05277)
- Adding in unicode characters for MatchedSubstring offset and length. [`432c133`](https://github.com/googlemaps/google-maps-services-java/commit/432c1332c86a6f5a26a96686417e24c4be205032)

## [v0.2.1](https://github.com/googlemaps/google-maps-services-java/compare/v0.2.0...v0.2.1) - 2017-08-02

### Merged

- ./gradlew googleJavaFormat [`#305`](https://github.com/googlemaps/google-maps-services-java/pull/305)
- Update GeoApiContext.java - Exception using GAE [`#304`](https://github.com/googlemaps/google-maps-services-java/pull/304)
- Upgrade mockwebserver to 3.8.1 from OkHttp distribution [`#301`](https://github.com/googlemaps/google-maps-services-java/pull/301)
- Suppress deprecation warnings for radarSearchQuery [`#302`](https://github.com/googlemaps/google-maps-services-java/pull/302)
- README: Make central repo links do searches for this lib  [`#300`](https://github.com/googlemaps/google-maps-services-java/pull/300)
- Add .nb-gradle files to .gitignore [`#299`](https://github.com/googlemaps/google-maps-services-java/pull/299)
- Spell 'adapter' consistently [`#298`](https://github.com/googlemaps/google-maps-services-java/pull/298)
- Update documentation publishing instructions [`#296`](https://github.com/googlemaps/google-maps-services-java/pull/296)
- GeocodingApiTest: fix missing quote in javadoc [`#294`](https://github.com/googlemaps/google-maps-services-java/pull/294)

### Commits

- Upgrade mockwebserver to 3.8.1 [`7d2282b`](https://github.com/googlemaps/google-maps-services-java/commit/7d2282be775badcabf6bd4af045ac9a6e090cfea)
- spell 'adapter' consistently [`5069f8a`](https://github.com/googlemaps/google-maps-services-java/commit/5069f8a8cba3f268731fd6e40bfd74c1f2abda03)
- README: Make central repo links do searches for this lib instead of going to main home page. [`d2e3245`](https://github.com/googlemaps/google-maps-services-java/commit/d2e3245992bde7b41b35a42977bb1270aa9fa76e)

## [v0.2.0](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.22...v0.2.0) - 2017-07-25

### Merged

- Landing Okhttp3 [`#293`](https://github.com/googlemaps/google-maps-services-java/pull/293)

### Commits

- Releasing Version 0.2.0 [`af0ca26`](https://github.com/googlemaps/google-maps-services-java/commit/af0ca2621ff8157a2be9f63988aa102f9613d6cd)
- Re-opening v0.2 in preparation for landing OkHttp3. [`b1e495e`](https://github.com/googlemaps/google-maps-services-java/commit/b1e495e376126138814da82fa771ea5b602cf981)

## [v0.1.22](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.21...v0.1.22) - 2017-07-24

### Merged

- Applying `google-java-format` to the codebase. [`#291`](https://github.com/googlemaps/google-maps-services-java/pull/291)
- Fix issue where Runnable runs on the incorrect thread [`#290`](https://github.com/googlemaps/google-maps-services-java/pull/290)

### Fixed

- A quick respin to fix https://github.com/googlemaps/google-maps-services-java/issues/292 [`#292`](https://github.com/googlemaps/google-maps-services-java/issues/292)

### Commits

- Reformat the static imports correctly [`94d8c8b`](https://github.com/googlemaps/google-maps-services-java/commit/94d8c8b1bf9a7963190ef90e8a80f2f338727f15)
- Making google-java-format easier to use. [`c9c006c`](https://github.com/googlemaps/google-maps-services-java/commit/c9c006cf226acb76ce75264a5b7701aebe9faa42)
- Runnable runs on the delegate ExecutorService [`eb81d7e`](https://github.com/googlemaps/google-maps-services-java/commit/eb81d7e318d41ee021f8addce7371904fd803dcd)

## [v0.1.21](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.20...v0.1.21) - 2017-07-03

### Merged

- Marking Radar Search as deprecated [`#284`](https://github.com/googlemaps/google-maps-services-java/pull/284)
- Converting tests to local server based. [`#282`](https://github.com/googlemaps/google-maps-services-java/pull/282)
- Converting Integration tests to Local Server tests [`#278`](https://github.com/googlemaps/google-maps-services-java/pull/278)
- Fixing license declaration [`#277`](https://github.com/googlemaps/google-maps-services-java/pull/277)
- Making PlaceDetailsRequest#Response public [`#276`](https://github.com/googlemaps/google-maps-services-java/pull/276)
- Adding undocumented address types. [`#275`](https://github.com/googlemaps/google-maps-services-java/pull/275)
- Testing Kita Ward. [`#274`](https://github.com/googlemaps/google-maps-services-java/pull/274)
- Test fix [`#273`](https://github.com/googlemaps/google-maps-services-java/pull/273)
- Replacing hand rolled rate limiter with Gauva's Rate Limiter. [`#272`](https://github.com/googlemaps/google-maps-services-java/pull/272)
- Adding example configuration for SLF4J to the README [`#271`](https://github.com/googlemaps/google-maps-services-java/pull/271)
- Making tests pass [`#269`](https://github.com/googlemaps/google-maps-services-java/pull/269)
- Add light_rail_station enum type [`#268`](https://github.com/googlemaps/google-maps-services-java/pull/268)
- Fix for issues 218,170 - aded instance creator for EncodedPolyline to use with Gson [`#260`](https://github.com/googlemaps/google-maps-services-java/pull/260)

### Commits

- Converting Places API Integration tests to local server tests. [`72edd9c`](https://github.com/googlemaps/google-maps-services-java/commit/72edd9c12c6726ff755f9cd1c58cd7689dacc125)
- Moving large string blobs to resource files [`21d2be5`](https://github.com/googlemaps/google-maps-services-java/commit/21d2be5eb9bf8a40b60079708ef78310e694090a)
- Converted last API surfaces to localtests. [`9379324`](https://github.com/googlemaps/google-maps-services-java/commit/9379324ef7b95a38f089b63e26e901b5c70f9aaf)

## [v0.1.20](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.19...v0.1.20) - 2017-04-13

### Merged

- Solving performance problems in parallel processing [`#259`](https://github.com/googlemaps/google-maps-services-java/pull/259)
- Fixing concurrency problem [`#255`](https://github.com/googlemaps/google-maps-services-java/pull/255)
- Fixing up ordering of signature calculation. [`#253`](https://github.com/googlemaps/google-maps-services-java/pull/253)

### Commits

- Created test for parallel signatures and cloned mac for each signature to avoid concurrency problems [`d9f55f9`](https://github.com/googlemaps/google-maps-services-java/commit/d9f55f91cac641407665a18f012ccb6a88db4445)
- Set maxQps on OKHttp dispatcher to not be limited to the default configuration [`fe422e9`](https://github.com/googlemaps/google-maps-services-java/commit/fe422e943eebc76e7592fa20e89b8f94f4d1fe48)
- Also catching exceptions during parallel signature to be more assertive [`4c41b2c`](https://github.com/googlemaps/google-maps-services-java/commit/4c41b2cbc8ab68d3e603d069a4e7ab5687496936)

## [v0.1.19](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.18...v0.1.19) - 2017-03-28

### Merged

- Version 0.1.19 release [`#252`](https://github.com/googlemaps/google-maps-services-java/pull/252)
- Fix for https://github.com/googlemaps/google-maps-services-java/issues/248 [`#251`](https://github.com/googlemaps/google-maps-services-java/pull/251)

### Commits

- Opening up development for v0.1.19 [`e2b474d`](https://github.com/googlemaps/google-maps-services-java/commit/e2b474d6a5191167251b83e77b1405651e775d23)
- Updating Javadoc link [`f9ade4e`](https://github.com/googlemaps/google-maps-services-java/commit/f9ade4ee593578c5d41903cf7c7a78f6f942e44f)

## [v0.1.18](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.17...v0.1.18) - 2017-03-24

### Merged

- Creating version 0.1.18 [`#247`](https://github.com/googlemaps/google-maps-services-java/pull/247)
- Updating gradle version [`#246`](https://github.com/googlemaps/google-maps-services-java/pull/246)
- Updating versions and making tests pass [`#245`](https://github.com/googlemaps/google-maps-services-java/pull/245)
- Make PendingResult#await throw specific Exception [`#238`](https://github.com/googlemaps/google-maps-services-java/pull/238)
- Linkify travis badge and mavencentral badge in README [`#239`](https://github.com/googlemaps/google-maps-services-java/pull/239)
- Update NearbySearchRequest.java [`#243`](https://github.com/googlemaps/google-maps-services-java/pull/243)
- Update build.gradle to require 1.7 [`#236`](https://github.com/googlemaps/google-maps-services-java/pull/236)
- Update TextSearchRequest.java [`#221`](https://github.com/googlemaps/google-maps-services-java/pull/221)
- Fixing tests [`#223`](https://github.com/googlemaps/google-maps-services-java/pull/223)
- Correct usage of GeoApiContext [`#222`](https://github.com/googlemaps/google-maps-services-java/pull/222)
- Support `nearestRoads` API call [`#217`](https://github.com/googlemaps/google-maps-services-java/pull/217)
- Add support for address type 'postal_code_prefix'. [`#215`](https://github.com/googlemaps/google-maps-services-java/pull/215)
- Tidying up headers and imports [`#213`](https://github.com/googlemaps/google-maps-services-java/pull/213)
- Save waypoints [`#204`](https://github.com/googlemaps/google-maps-services-java/pull/204)
- Fixing up broken merge [`#212`](https://github.com/googlemaps/google-maps-services-java/pull/212)
- Change Java Util Logging to SLF4J and add SLF4J simple logger for tests [`#186`](https://github.com/googlemaps/google-maps-services-java/pull/186)
- Update Javadoc reference [`#211`](https://github.com/googlemaps/google-maps-services-java/pull/211)
- Open up Version 0.1.18 for development [`#210`](https://github.com/googlemaps/google-maps-services-java/pull/210)

### Commits

- Add waypoint optimization tests [`01f6a29`](https://github.com/googlemaps/google-maps-services-java/commit/01f6a29fa3250c22fdf93d77f9c5c75648387873)
- Remove unused ExceptionResult [`fa50c98`](https://github.com/googlemaps/google-maps-services-java/commit/fa50c988c614cf3540a1e79b74736ac3f35ce9f0)
- Support nearestRoads method from Roads API [`0829c7e`](https://github.com/googlemaps/google-maps-services-java/commit/0829c7e6cbd128faf2457b63bcef27730a76a9ff)

## [v0.1.17](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.16...v0.1.17) - 2016-11-30

### Merged

- Version 0.1.17 [`#209`](https://github.com/googlemaps/google-maps-services-java/pull/209)
- Adding Custom Parameter handling [`#208`](https://github.com/googlemaps/google-maps-services-java/pull/208)
- Keep using ThreadPoolExecutor but increase number of threads [`#199`](https://github.com/googlemaps/google-maps-services-java/pull/199)
- Add waypoints method that takes in an array of LatLng [`#205`](https://github.com/googlemaps/google-maps-services-java/pull/205)
- Add Light Rail Station to AddressType [`#197`](https://github.com/googlemaps/google-maps-services-java/pull/197)
- Update documentation links that were outdated. [`#194`](https://github.com/googlemaps/google-maps-services-java/pull/194)
- Add support for multiple type for PlacesApi [`#192`](https://github.com/googlemaps/google-maps-services-java/pull/192)
- Allow specific exception types to be retried or not retried [`#189`](https://github.com/googlemaps/google-maps-services-java/pull/189)

### Commits

- Allow specific exception types to be retried or not retried. [`5f55a2b`](https://github.com/googlemaps/google-maps-services-java/commit/5f55a2b7b6a1b06390a779f56afe5a4d7546cfc4)
- Refactor reading test response file into a TestUtils. [`3f324a7`](https://github.com/googlemaps/google-maps-services-java/commit/3f324a744c65d27d7beee4e4ce6982611ec78342)
- Adding custom parameter pass through [`272734d`](https://github.com/googlemaps/google-maps-services-java/commit/272734d793df6708b1aafa7db4b0b5441f109efd)

## [v0.1.16](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.15...v0.1.16) - 2016-10-06

### Merged

- Checking if OVER_QUERY_LIMIT is caused because Daily limit. In that c… [`#188`](https://github.com/googlemaps/google-maps-services-java/pull/188)
- Allow retries to be limited by number of retries, not time. [`#185`](https://github.com/googlemaps/google-maps-services-java/pull/185)
- Adding support for the Geolocation API call. [`#164`](https://github.com/googlemaps/google-maps-services-java/pull/164)
- Adding support for maneuver in the directions api. [`#50`](https://github.com/googlemaps/google-maps-services-java/pull/50)
- Fix poor resource management code (fixes #179) [`#181`](https://github.com/googlemaps/google-maps-services-java/pull/181)
- Add support for the 'permanently_closed' attribute in Place responses. [`#177`](https://github.com/googlemaps/google-maps-services-java/pull/177)
- Set RateLimitExecutor thread name. [`#174`](https://github.com/googlemaps/google-maps-services-java/pull/174)
- Cleaning up broken tests [`#167`](https://github.com/googlemaps/google-maps-services-java/pull/167)
- Adds client-id example to README.md [`#166`](https://github.com/googlemaps/google-maps-services-java/pull/166)

### Fixed

- Merge pull request #181 from ben-manes/master [`#179`](https://github.com/googlemaps/google-maps-services-java/issues/179)
- Fix poor resource management code (fixes #179) [`#179`](https://github.com/googlemaps/google-maps-services-java/issues/179)

### Commits

- Issue Fixes [`5281ed9`](https://github.com/googlemaps/google-maps-services-java/commit/5281ed9894a15c38fdddd9249cc8ca179d54763b)
- Pull Request Comments low hanging fruit. [`b2c718a`](https://github.com/googlemaps/google-maps-services-java/commit/b2c718a94828dfbb262105f46f8025efdb669902)
- ApiConfig now controls GET vs POST. [`f98913a`](https://github.com/googlemaps/google-maps-services-java/commit/f98913ab06475477298b3dfafea882883427fd5f)

## [v0.1.15](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.12...v0.1.15) - 2016-06-07

### Merged

- Version 0.1.15 release [`#159`](https://github.com/googlemaps/google-maps-services-java/pull/159)
- Break fixes [`#158`](https://github.com/googlemaps/google-maps-services-java/pull/158)
- App engine support [`#154`](https://github.com/googlemaps/google-maps-services-java/pull/154)
- Increasing the precision of LatLng's toUrlValue [`#153`](https://github.com/googlemaps/google-maps-services-java/pull/153)
- Adding canonical literals for AddressComponentType [`#152`](https://github.com/googlemaps/google-maps-services-java/pull/152)
- Upgrade OkHttp [`#144`](https://github.com/googlemaps/google-maps-services-java/pull/144)

### Fixed

- Upgrading OkHTTP to current. [`#143`](https://github.com/googlemaps/google-maps-services-java/issues/143)

### Commits

- Adding support for Google App Engine [`e59c5d3`](https://github.com/googlemaps/google-maps-services-java/commit/e59c5d3e8116aa2393c7ed4cc0d53ad3669066f2)
- Adding unit test for canonical literals [`65e6997`](https://github.com/googlemaps/google-maps-services-java/commit/65e69970f23b649b15530232c513fe35cea4db17)
- Adding copyright notices. [`7755784`](https://github.com/googlemaps/google-maps-services-java/commit/7755784e0072403dd2c6e28db3877843ac20b177)

## [v0.1.12](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.11...v0.1.12) - 2016-03-24

### Merged

- Dropping flaky tests. [`#142`](https://github.com/googlemaps/google-maps-services-java/pull/142)
- Fix places autocomplete types parameter and introduced correct type enum [`#140`](https://github.com/googlemaps/google-maps-services-java/pull/140)
- distance matrix request - add traffic_model to request and durationInTraffic to response [`#139`](https://github.com/googlemaps/google-maps-services-java/pull/139)
- Commenting out Flaky test [`#136`](https://github.com/googlemaps/google-maps-services-java/pull/136)
- Bug fixes and cleanups [`#135`](https://github.com/googlemaps/google-maps-services-java/pull/135)

### Commits

- Fix for https://github.com/googlemaps/google-maps-services-java/issues/75 [`99b8bd8`](https://github.com/googlemaps/google-maps-services-java/commit/99b8bd86f2f2cd639600447c05d2d9603ab58b76)
- Making Places API return AddressTypes instead of Strings. [`d567486`](https://github.com/googlemaps/google-maps-services-java/commit/d5674861aee3d96e0f7952f88afdc4feca94550e)
- Changing local_icon to localIcon, and reverting AddressType change. [`54b685e`](https://github.com/googlemaps/google-maps-services-java/commit/54b685ec3cd1cb71bd57765625b68c65934df861)

## [v0.1.11](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.9...v0.1.11) - 2016-02-18

### Merged

- Adding most of Places API, plus tidy ups. [`#133`](https://github.com/googlemaps/google-maps-services-java/pull/133)
- Fixing analytics. [`#130`](https://github.com/googlemaps/google-maps-services-java/pull/130)
- Making the fares tests pass. [`#129`](https://github.com/googlemaps/google-maps-services-java/pull/129)
- NPE check for route fares, and a better test for permanently closed. [`#128`](https://github.com/googlemaps/google-maps-services-java/pull/128)
- Adding tests: UTF8 return parsing and permanently closed [`#127`](https://github.com/googlemaps/google-maps-services-java/pull/127)
- Making Travis compile again. [`#125`](https://github.com/googlemaps/google-maps-services-java/pull/125)
- Tidyup tests + better javadoc. [`#124`](https://github.com/googlemaps/google-maps-services-java/pull/124)
- Introducing Geocoded Waypoints into Directions API result. [`#118`](https://github.com/googlemaps/google-maps-services-java/pull/118)
- Places API test fixes [`#115`](https://github.com/googlemaps/google-maps-services-java/pull/115)

### Commits

- Simplified Directions API to just one result type [`f1f73ba`](https://github.com/googlemaps/google-maps-services-java/commit/f1f73badbd38da3245c25fca8f50dfc32cca4912)
- Splitting the full result from just the routes. [`13e24c1`](https://github.com/googlemaps/google-maps-services-java/commit/13e24c1bfffc112c53fcd0e8d48f876a2d68854d)
- Introducing GeocodedWaypointStatus. [`b05e5ae`](https://github.com/googlemaps/google-maps-services-java/commit/b05e5aebbac63179d18569e76a79c896b9e91f40)

## [v0.1.9](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.8...v0.1.9) - 2015-11-10

### Merged

- Added traffic_model parameter to directions requests [`#112`](https://github.com/googlemaps/google-maps-services-java/pull/112)
- Adding Subway Station [`#111`](https://github.com/googlemaps/google-maps-services-java/pull/111)
- Places API release - version 0.1.8 [`#110`](https://github.com/googlemaps/google-maps-services-java/pull/110)

### Commits

- version bump [`dfa0a2f`](https://github.com/googlemaps/google-maps-services-java/commit/dfa0a2fa2ed9e1ef5974239dadedd20a14aa90a8)

## [v0.1.8](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.7...v0.1.8) - 2015-10-14

### Merged

- Fixing javadoc errors. [`#107`](https://github.com/googlemaps/google-maps-services-java/pull/107)
- Adding Places API [`#106`](https://github.com/googlemaps/google-maps-services-java/pull/106)
- Update README.md [`#103`](https://github.com/googlemaps/google-maps-services-java/pull/103)
- Making tests happy. [`#100`](https://github.com/googlemaps/google-maps-services-java/pull/100)
- Added channel parameter to request (issue #77) [`#97`](https://github.com/googlemaps/google-maps-services-java/pull/97)
- Multiple failed test fixes [`#89`](https://github.com/googlemaps/google-maps-services-java/pull/89)
- For issue #87 NumberFormatException when parsing SpeedLimit  [`#88`](https://github.com/googlemaps/google-maps-services-java/pull/88)

### Commits

- Fix for issue #87 NumberFormatException when parsing SpeedLimit response for nonmetric countries. Changed SpeedLimit.speedLimit property from long to double. Added unit tests. [`17342b0`](https://github.com/googlemaps/google-maps-services-java/commit/17342b0b455da698ee3e2d2bddb1d2fbabbb9aa5)
- Code tidyup [`e469d56`](https://github.com/googlemaps/google-maps-services-java/commit/e469d5623eb1ebd50fe9a9105bc56677b77c2550)
- Enable using API Keys as systemProperties. [`7e030c4`](https://github.com/googlemaps/google-maps-services-java/commit/7e030c493295061ee2c92288b60b621d9aec3bcb)

## [v0.1.7](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.6...v0.1.7) - 2015-05-05

### Merged

- Added postal_code_prefix (Closes #48) [`#86`](https://github.com/googlemaps/google-maps-services-java/pull/86)
- Fixes vehicle type field name [`#81`](https://github.com/googlemaps/google-maps-services-java/pull/81)
- Added some returning but not documented AddressType. [`#78`](https://github.com/googlemaps/google-maps-services-java/pull/78)
- Updated build rules to execute M4W + keyed reqs together [`#80`](https://github.com/googlemaps/google-maps-services-java/pull/80)

### Fixed

- Merge pull request #86 from markmcd/prefix [`#48`](https://github.com/googlemaps/google-maps-services-java/issues/48)
- Added postal_code_prefix (Closes #48) [`#48`](https://github.com/googlemaps/google-maps-services-java/issues/48)

### Commits

- Bumped timeouts for Raods API tests [`57fe118`](https://github.com/googlemaps/google-maps-services-java/commit/57fe118ba46572349f5ac33063be15e332add23c)
- JavaDoc in README to 0.1.6 [`270be23`](https://github.com/googlemaps/google-maps-services-java/commit/270be23a25851763530b6faf18545da6db9d3043)
- Adding @NicolasPoirier to AUTH/CONTRIB [`835fd60`](https://github.com/googlemaps/google-maps-services-java/commit/835fd6092ed26a2730b5304da71fe0c6d739c767)

## [v0.1.6](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.5...v0.1.6) - 2015-03-04

### Merged

- Clarified support text [`#44`](https://github.com/googlemaps/google-maps-services-java/pull/44)
- Revert support level to Java 7 [`#66`](https://github.com/googlemaps/google-maps-services-java/pull/66)
- Add transit details [`#72`](https://github.com/googlemaps/google-maps-services-java/pull/72)
- Added place ID support to geocoding [`#73`](https://github.com/googlemaps/google-maps-services-java/pull/73)
- Fixed up polyline doc [`#74`](https://github.com/googlemaps/google-maps-services-java/pull/74)
- Fixed direction steps field name [`#71`](https://github.com/googlemaps/google-maps-services-java/pull/71)
- Adds Proxy support to GeoApiContext [`#67`](https://github.com/googlemaps/google-maps-services-java/pull/67)
- Fixed broken tests [`#64`](https://github.com/googlemaps/google-maps-services-java/pull/64)
- final on String in model [`#63`](https://github.com/googlemaps/google-maps-services-java/pull/63)
- :moneybag: Transit Fares in Directions & DistanceMatrix [`#60`](https://github.com/googlemaps/google-maps-services-java/pull/60)
- Fixed broken test caused by API data [`#61`](https://github.com/googlemaps/google-maps-services-java/pull/61)
- Added ApiConfig for more configurable API endpoints [`#58`](https://github.com/googlemaps/google-maps-services-java/pull/58)

### Commits

- :car: Added Roads API [`a69e13c`](https://github.com/googlemaps/google-maps-services-java/commit/a69e13c5ec38020a607c5b2562b84ee197c7d2ce)
- Optimized lookup() method, adding more types. [`b0b1301`](https://github.com/googlemaps/google-maps-services-java/commit/b0b1301e7664d6a395d4d44ac64b8ad075bcf170)
- Added transit fares to directions API [`efb08a9`](https://github.com/googlemaps/google-maps-services-java/commit/efb08a9838cc75e37fd2169f3ad7515c1c9a584d)

## [v0.1.5](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.4...v0.1.5) - 2015-01-19

### Merged

- :wrench: Updated GSON dep to working version [`#57`](https://github.com/googlemaps/google-maps-services-java/pull/57)
- Change req to Java 1.6 [`#56`](https://github.com/googlemaps/google-maps-services-java/pull/56)
- Response parsing and dependency on StandardCharsets class [`#46`](https://github.com/googlemaps/google-maps-services-java/pull/46)

### Commits

- dependency on java.nio removed [`07a3098`](https://github.com/googlemaps/google-maps-services-java/commit/07a30986894a2b5735181623265c1ce46d8621f9)
- Adding support for maneuver in the directions api. [`0f7117d`](https://github.com/googlemaps/google-maps-services-java/commit/0f7117d685056db5954c8ffb57529b2f6e3d34cc)
- a bit smaller buffer [`267eb3d`](https://github.com/googlemaps/google-maps-services-java/commit/267eb3d530524a79197df2fed2d8afd8a1eef2f4)

## [v0.1.4](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.3...v0.1.4) - 2014-11-04

### Merged

- Minor cleanups, refactor RateLimitExecutorService. [`#41`](https://github.com/googlemaps/google-maps-services-java/pull/41)
- Add @nutsiepully, github usernames to CONTRIBUTORS [`#43`](https://github.com/googlemaps/google-maps-services-java/pull/43)
- Porting code to be compatible with Java 1.6 [`#42`](https://github.com/googlemaps/google-maps-services-java/pull/42)
- Fixing .gitignore to remove iml and intellij idea files. [`#40`](https://github.com/googlemaps/google-maps-services-java/pull/40)
- Make DistanceMatrixElementStatus an enum. [`#39`](https://github.com/googlemaps/google-maps-services-java/pull/39)
- Add extra safety to unmarshaling to enums. [`#38`](https://github.com/googlemaps/google-maps-services-java/pull/38)

### Commits

- Incorporating formatting feedback. [`98785ca`](https://github.com/googlemaps/google-maps-services-java/commit/98785ca551959b8e77fdfec10ea8955fe3878011)
- :sparkle: clarified support text [`2c4a7cc`](https://github.com/googlemaps/google-maps-services-java/commit/2c4a7cc2c9829c51726260e925fe61d4128b97d8)
- javadoc -&gt; 0.1.3 [`1d144d1`](https://github.com/googlemaps/google-maps-services-java/commit/1d144d1796a9c60dec37d379ca695fa2fa9e3d47)

## [v0.1.3](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.2...v0.1.3) - 2014-10-03

### Merged

- Added logging to the rate limiting test to aid debugging if it fails again [`#36`](https://github.com/googlemaps/google-maps-services-java/pull/36)
- Don't throw exceptions when the server returns something we're not expecting. [`#34`](https://github.com/googlemaps/google-maps-services-java/pull/34)
- Fix indentation. [`#33`](https://github.com/googlemaps/google-maps-services-java/pull/33)
- Better handling of more address types and address component types [`#32`](https://github.com/googlemaps/google-maps-services-java/pull/32)
- Fixing a typo in ReadMe.md [`#29`](https://github.com/googlemaps/google-maps-services-java/pull/29)
- A bit more Maps for Business -&gt; Maps for Work renaming. [`#27`](https://github.com/googlemaps/google-maps-services-java/pull/27)
- Updates to the ReadMe. Cleaning up the original documentation, and clarifying some things around support/contribution. [`#26`](https://github.com/googlemaps/google-maps-services-java/pull/26)
- Remove elevation LatLng join code [`#25`](https://github.com/googlemaps/google-maps-services-java/pull/25)

### Commits

- Fix indentation. Remove unneeded import/impl of UrlValue. [`3f46121`](https://github.com/googlemaps/google-maps-services-java/commit/3f46121a8cacbe3c4abfc1cf1b2828b270ef8dcf)
- Updates to the ReadMe file [`989be08`](https://github.com/googlemaps/google-maps-services-java/commit/989be08cc55e370884a81e4a5bd4ef2987749762)
- Add instructions for generating and pushing javadoc. [`be0b96e`](https://github.com/googlemaps/google-maps-services-java/commit/be0b96e16c522d5b03f2c10524646340acfe25cf)

## [v0.1.2](https://github.com/googlemaps/google-maps-services-java/compare/v0.1.1...v0.1.2) - 2014-09-16

### Merged

- :vertical_traffic_light: added over_query_limit to retry logic [`#22`](https://github.com/googlemaps/google-maps-services-java/pull/22)
- :curly_loop: fixed long-running threads [`#21`](https://github.com/googlemaps/google-maps-services-java/pull/21)

### Commits

- :curly_loop: extracted threadFactory [`14c7341`](https://github.com/googlemaps/google-maps-services-java/commit/14c7341afe6fdf9b30be9be808f0be2cb199f8c1)
- added over_query_limit to retry logic [`c074753`](https://github.com/googlemaps/google-maps-services-java/commit/c0747535ff208a846f2f77d0babc4530a922c6a9)
- :curly_loop: updated comment regarding thread termination [`d5af2a1`](https://github.com/googlemaps/google-maps-services-java/commit/d5af2a1764973e384c0da63e85adf8e3d75b285e)

## v0.1.1 - 2014-09-12

### Merged

- Fixed signing rules to allow travis to run [`#20`](https://github.com/googlemaps/google-maps-services-java/pull/20)
- Added maven deployment tasks [`#19`](https://github.com/googlemaps/google-maps-services-java/pull/19)
- Updated partial match test to use a working query [`#18`](https://github.com/googlemaps/google-maps-services-java/pull/18)
- Update README.md [`#16`](https://github.com/googlemaps/google-maps-services-java/pull/16)
- Enable test coverage reports with jacoco [`#13`](https://github.com/googlemaps/google-maps-services-java/pull/13)
- Don't use local hostname for MockWebServer's base URL. [`#14`](https://github.com/googlemaps/google-maps-services-java/pull/14)
- added NOT_FOUND exception (closes #10) [`#11`](https://github.com/googlemaps/google-maps-services-java/pull/11)
- :construction: fixed javadoc warnings [`#9`](https://github.com/googlemaps/google-maps-services-java/pull/9)
- Updating copyrights and license [`#8`](https://github.com/googlemaps/google-maps-services-java/pull/8)
- Set up tests to run with client ID too [`#7`](https://github.com/googlemaps/google-maps-services-java/pull/7)
- Moved QPS args from constructor to setter. [`#6`](https://github.com/googlemaps/google-maps-services-java/pull/6)
- Added README [`#2`](https://github.com/googlemaps/google-maps-services-java/pull/2)
- Version is now specified in build file [`#4`](https://github.com/googlemaps/google-maps-services-java/pull/4)
- Adds copyright string to each file [`#5`](https://github.com/googlemaps/google-maps-services-java/pull/5)
- Adding files for license, (c) and contributors [`#3`](https://github.com/googlemaps/google-maps-services-java/pull/3)
- Migrated build to gradle [`#1`](https://github.com/googlemaps/google-maps-services-java/pull/1)

### Fixed

- Merge pull request #11 from markmcd/issue-10 [`#10`](https://github.com/googlemaps/google-maps-services-java/issues/10)
- added NOT_FOUND exception (closes #10) [`#10`](https://github.com/googlemaps/google-maps-services-java/issues/10)

### Commits

- Initial import of Java code [`b577f30`](https://github.com/googlemaps/google-maps-services-java/commit/b577f300c18b88674a375018bfc4c20daca1f24b)
- Using the /* */ format instead of // [`0816981`](https://github.com/googlemaps/google-maps-services-java/commit/081698119beb67d76d0808db23bbc5fb615b08a8)
- :gift: Added gradle wrapper [`d669a10`](https://github.com/googlemaps/google-maps-services-java/commit/d669a10855a019db6332f3cbaf354d2d24e26a22)
