# Testcontainers Common

Common setup code for working with Testcontainers in JUnit 5.

## How to Use

Simply add this extension to any test class:

```kotlin
@ExtendWith(TestContainersExtension::class)
```

## How to Configure

This supports Postgres & Keycloak. A file called `testcontainers-common.yml` must exist on the classpath. It should have the following properties

```yaml
# To enable/disable and configure postgres:
postgres:
  enable: true
  # The following properties are set by default if not specified here. The keys they are set to can be overridden here, though.
  propertyMappings:
    postgres.url: spring.datasource.url
    postgres.username: spring.datasource.username
    postgres.password: spring.datasource.password
# To enable/disable and configure keycloak
keycloak:
  enable: true
  # The following properties are set by default if not specified here. The keys they are set to can be overridden here, though.
  propertyMappings:
    keycloak.url: keycloak.auth-server-url
```

## How to Reuse

This project supports container re-use. Please see the Testcontainers documentation for details.