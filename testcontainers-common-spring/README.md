# Testcontainers Common - Spring

A Spring wrapper for common code for working with Testcontainers in JUnit 5.

## How to Use

Simply add this extension to any test class:

```kotlin
@ExtendWith(SpringTestContainersExtension::class)
```

## How to Configure

Add the following to your test `application.yml`. This controls which containers are enabled. All are enabled by default.

```yaml
testcontainers:
  common:
    postgres:
      enable: true
    keycloak:
      enable: true
    mongodb:
      enable: true
```

## Container Settings

See [testcontainers-common README](../testcontainers-common/README.md)

## System Properties Set

See [testcontainers-common README](../testcontainers-common/README.md)

## How to Reuse

See [testcontainers-common README](../testcontainers-common/README.md)