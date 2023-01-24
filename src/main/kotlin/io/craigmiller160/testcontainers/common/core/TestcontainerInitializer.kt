package io.craigmiller160.testcontainers.common.core

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import io.craigmiller160.testcontainers.common.utils.Terminal
import java.nio.file.Paths
import org.testcontainers.containers.PostgreSQLContainer

object TestcontainerInitializer {
  fun initialize(config: TestcontainersCommonConfig): ContainerInitializationResult {
    val (postgresStatus, postgresContainer) =
      if (config.postgres?.enable == true) {
        startPostgresContainer(config.postgres)
      } else {
        ContainerStatus.DISABLED to null
      }

    val (keycloakStatus, keycloakContainer) =
      if (config.keycloak?.enable == true) {
        startKeycloakContainer(config.keycloak)
      } else {
        ContainerStatus.DISABLED to null
      }

    return ContainerInitializationResult(
      postgresStatus = postgresStatus,
      keycloakStatus = keycloakStatus,
      postgresContainer = postgresContainer,
      keycloakContainer = keycloakContainer)
  }

  private fun startKeycloakContainer(
    config: ContainerConfig
  ): Pair<ContainerStatus, KeycloakContainer> {
    val container =
      KeycloakContainer(TestcontainerConstants.KEYCLOAK_IMAGE)
        .withAdminUsername(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
        .withAdminPassword(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
        .withRealmImportFile(TestcontainerConstants.KEYCLOAK_REALM_FILE)
        .withReuse(true)
        .also { it.start() }
    setProperty(
      config.propertyMappings, TestcontainerConstants.KEYCLOAK_URL_PROP, container.authServerUrl)
    setProperty(
      config.propertyMappings,
      TestcontainerConstants.KEYCLOAK_REALM_PROP,
      TestcontainerConstants.KEYCLOAK_REALM)
    setProperty(
      config.propertyMappings,
      TestcontainerConstants.KEYCLOAK_CLIENT_ID_PROP,
      TestcontainerConstants.KEYCLOAK_CLIENT_ID)
    setProperty(
      config.propertyMappings,
      TestcontainerConstants.KEYCLOAK_CLIENT_SECRET_PROP,
      TestcontainerConstants.KEYCLOAK_CLIENT_SECRET)
    return ContainerStatus.STARTED to container
  }

  private fun setProperty(
    propertyMappings: Map<String, String>,
    defaultKey: String,
    value: String
  ) {
    propertyMappings[defaultKey]?.let { System.setProperty(it, value) }
      ?: System.setProperty(defaultKey, value)
  }

  fun getDatabaseName(): String {
    val path = Paths.get(Terminal.execute("pwd")).fileName
    println("PATH: $path") // TODO delete this
    return "test"
  }

  private fun startPostgresContainer(
    config: ContainerConfig
  ): Pair<ContainerStatus, PostgreSQLContainer<*>> {
    val container =
      PostgreSQLContainer(TestcontainerConstants.POSTGRES_IMAGE)
        .withUsername(TestcontainerConstants.POSTGRES_USER)
        .withPassword(TestcontainerConstants.POSTGRES_PASSWORD)
        .withDatabaseName(getDatabaseName())
        .withReuse(true)
        .also { it.start() }
    setProperty(
      config.propertyMappings, TestcontainerConstants.POSTGRES_URL_PROP, container.jdbcUrl)
    setProperty(
      config.propertyMappings, TestcontainerConstants.POSTGRES_PASSWORD_PROP, container.password)
    setProperty(
      config.propertyMappings, TestcontainerConstants.POSTGRES_USER_PROP, container.username)
    return ContainerStatus.STARTED to container
  }
}
