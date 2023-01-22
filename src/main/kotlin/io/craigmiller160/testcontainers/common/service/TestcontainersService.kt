package io.craigmiller160.testcontainers.common.service

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.testcontainers.containers.PostgreSQLContainer

object TestcontainersService {
  fun startContainers(config: TestcontainersCommonConfig) {
    if (config.postgres?.enable == true) {
      startPostgresContainer()
    }

    if (config.keycloak?.enable == true) {
      startKeycloakContainer()
    }
  }

  // TODO setup constants

  private fun startKeycloakContainer() {
    val container =
      KeycloakContainer("keycloak:20.0.2")
        .withRealmImportFile("keycloak-realm.json")
        .withReuse(true)
        .also { it.start() }
    System.setProperty("keycloak.auth-server-url", container.authServerUrl)
  }

  private fun startPostgresContainer() {
    val container =
      PostgreSQLContainer("postgres:14.5")
        .withUsername("username")
        .withPassword("password")
        .withDatabaseName("test")
        .withReuse(true)
        .also { it.start() }
    System.setProperty("postgres.url", container.jdbcUrl)
    System.setProperty("postgres.password", container.password)
    System.setProperty("postgres.username", container.username)
  }
}
