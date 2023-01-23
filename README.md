# Testcontainers Common

Common setup code for working with Testcontainers in JUnit 5.

## How to Use

Simply add this extension to any test class:

```kotlin
@ExtendWith(TestContainersExtension::class)
```

## How to Configure

This supports Postgres & Keycloak. Provide the following configuration file at the root of your classpath to enable them:

```yaml
# testcontainers-common.yml
postgres:
  enable: true
keycloak:
  enable: true
```

## How to Reuse

This project supports container re-use. Please see the Testcontainers documentation for details.