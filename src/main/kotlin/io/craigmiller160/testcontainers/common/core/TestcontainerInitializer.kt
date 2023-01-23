package io.craigmiller160.testcontainers.common.core

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.testcontainers.containers.PostgreSQLContainer

object TestcontainerInitializer {
  fun initialize(config: TestcontainersCommonConfig): ContainerInitializationResult {
    val (postgresStatus, postgresContainer) =
      if (config.postgres?.enable == true) {
        startPostgresContainer()
      } else {
        ContainerStatus.DISABLED to null
      }

    val (keycloakStatus, keycloakContainer) =
      if (config.keycloak?.enable == true) {
        startKeycloakContainer()
      } else {
        ContainerStatus.DISABLED to null
      }

    return ContainerInitializationResult(
      postgresStatus = postgresStatus,
      keycloak = keycloakStatus,
      postgresContainer = postgresContainer,
      keycloakContainer = keycloakContainer)
  }

  private fun startKeycloakContainer(): Pair<ContainerStatus, KeycloakContainer> {
    val container =
      KeycloakContainer(TestcontainerConstants.KEYCLOAK_IMAGE)
        .withAdminUsername(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
        .withAdminPassword(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
        .withRealmImportFile(TestcontainerConstants.KEYCLOAK_REALM_FILE)
        .withReuse(true)
        .also { it.start() }
    System.setProperty(TestcontainerConstants.KEYCLOAK_URL_PROP, container.authServerUrl)
    return ContainerStatus.STARTED to container
  }

  private fun startPostgresContainer(): Pair<ContainerStatus, PostgreSQLContainer<*>> {
    val container =
      PostgreSQLContainer(TestcontainerConstants.POSTGRES_IMAGE)
        .withUsername(TestcontainerConstants.POSTGRES_USER)
        .withPassword(TestcontainerConstants.POSTGRES_PASSWORD)
        .withDatabaseName(TestcontainerConstants.POSTGRES_DB_NAME)
        .withReuse(true)
        .also { it.start() }
    System.setProperty(TestcontainerConstants.POSTGRES_URL_PROP, container.jdbcUrl)
    System.setProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP, container.password)
    System.setProperty(TestcontainerConstants.POSTGRES_USER_PROP, container.username)
    return ContainerStatus.STARTED to container
  }
}
