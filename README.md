# Fees Java Client

[![Build Status](https://travis-ci.com/hmcts/fees-java-client.svg?branch=master)](https://travis-ci.com/hmcts/fees-java-client)
[ ![Download](https://api.bintray.com/packages/hmcts/hmcts-maven/fees-java-client/images/download.svg) ](https://bintray.com/hmcts/hmcts-maven/fees-java-client/_latestVersion)

This is a client library for interacting with the Fees application.

## Getting started

### Prerequisites

- [JDK 8](https://www.oracle.com/java)

## Usage

Just include the library as your dependency and you will be to use the client class.
You will also need to set the spring configuration property of `fees.api.url`

A client (FeesClient) is provided for interacting with the FeesApi feign client to simplify the flow:
```java
@Service
class FeesService {
    private final FeesClient feesClient;
    
    FeesService(FeesClient feesClient) {
        this.feesClient = feesClient;
    }
    
    public FeeLookupResponseDto lookupFee(String channel, String event, BigDecimal amount) {
        return feesClient.lookupFee(channel, event, amount);
    }

    public Fee2Dto[] findRangeGroup(String channel, String event) {
        return feesClient.findRangeGroup(channel, event);
    }
}
```

To use the client, you should also define appropriate values for:
- `fees.api.service`,
- `fees.api.jurisdiction1`, and
- `fees.api.jurisdiction2`.

Components provided by this library will get automatically configured in a Spring context if `fees.api.url` configuration property is defined and does not equal `false`. 

## Building

The project uses [Gradle](https://gradle.org) as a build tool but you don't have install it locally since there is a
`./gradlew` wrapper script.  

To build project please execute the following command:

```bash
    ./gradlew build
```

## Developing

### Coding style tests

To run all checks (including unit tests) please execute the following command:

```bash
    ./gradlew check
```

## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

To release a new version add a tag with the version number and push this up to the origin repository. This will then 
build and publish the release to maven.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
