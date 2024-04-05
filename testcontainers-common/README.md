# Testcontainers Common

Common setup code for working with Testcontainers in JUnit 5.

## How to Use

Simply add this extension to any test class:

```kotlin
@ExtendWith(TestContainersExtension::class)
```

## How to Configure

This supports Postgres, Keycloak, and MongoDB. A file called `testcontainers-common.yml` must exist on the classpath. It should have the following properties:

```yaml
postgres:
  enable: true
keycloak:
  enable: true
mongodb:
  enable: true
```

## Container Settings

### Ports

The containers created by this library are bound to the following local ports.

| Container | Port  |
|-----------|-------|
| Postgres  | 5433  |
| Keycloak  | 8081  |
| Mongo     | 27018 |

### Additional Settings

- Postgres stores everything in a database called `test`.
- Postgres schema name is derived from the project directory, with the schema name derived from the project directory. The schema name has `-` replaced with `_`, so `expense-tracker-api` becomes the schema `expense_tracker_api`.
- MongoDB stores everything in a database called `test`.
- MongoDB has no authentication. It is not properly supported by the testcontainer yet.

## System Properties Set

To support connecting to the containers, the following system properties are set after container startup.

### Postgres Properties

| Property                                | Description                                                                              |
|-----------------------------------------|------------------------------------------------------------------------------------------|
| testcontainers.common.postgres.jdbcUrl  | The full JDBC URL for Postgres                                                           |
| testcontainers.common.postgres.r2dbcUrl | The full R2DBC URL for Postgres                                                          |
| testcontainers.common.postgres.user     | The username for Postgres                                                                |  
| testcontainers.common.postgres.password | The password for Postgres                                                                |
| testcontainers.common.postgres.schema   | The schema name for the tests for this application (better supports reuse of containers) |

### Keycloak Properties

| Property                                      | Description                               |
|-----------------------------------------------|-------------------------------------------|
| testcontainers.common.keycloak.url            | The full URL for the Keycloak auth server |
| testcontainers.common.keycloak.admin.user     | The username for the Keycloak admin user  |
| testcontainers.common.keycloak.admin.password | The password for the Keycloak admin user  |
| testcontainers.common.keycloak.realm          | The realm setup in Keycloak               |
| testcontainers.common.keycloak.client.id      | The test Client ID in Keycloak            |
| testcontainers.common.keycloak.client.secret  | The test Client Secret in Keycloak        |

### MongoDB Properties

| Property                          | Description                    |
|-----------------------------------|--------------------------------|
| testcontainers.common.mongodb.url | The connection URL for MongoDB |

## How to Reuse

This project supports container re-use. Please see the Testcontainers documentation for details.