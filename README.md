# Testcontainers Common

Common setup code for working with Testcontainers in JUnit 5.

## How to Use

Simply add this extension to any test class:

```kotlin
@ExtendWith(TestContainersExtension::class)
```

## How to Configure

This supports Postgres & Keycloak. A file called `testcontainers-common.yml` must exist on the classpath. It should have the following properties:

```yaml
postgres:
  enable: true
  propertyMappings:
    # Optional re-mapping of properties set once the container starts
keycloak:
  enable: true
  propertyMappings:
    # Optional re-mapping of properties set once the container starts
```

For the property mappings, critical properties related to the containers are set as system properties. They are set with default keys. To re-map them to custom keys, add the mappings in the `propertyMappings` section of that container. The default keys are:

```
postgres.url
postgres.user
postgres.password
keycloak.url
keycloak.realm
keycloak.client.id
keycloak.client.secret
```

## How to Reuse

This project supports container re-use. Please see the Testcontainers documentation for details.