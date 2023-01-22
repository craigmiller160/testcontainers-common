package io.craigmiller160.testcontainers.common

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.DefaultConfigResolver
import io.craigmiller160.testcontainers.common.utils.Terminal
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer

class TestcontainersExtension : BeforeAllCallback {
  private val resolver = DefaultConfigResolver()

  override fun beforeAll(context: ExtensionContext) {
    val config = resolver.resolve()
    val containers =
      runCatching { Terminal.runCommand("docker ps") }
        .map { getContainers(it) }
        .getOrElse { listOf() }

    if (config.postgres.enable && !containers.contains(config.postgres.externalName)) {
      startPostgresContainer(config.postgres)
    }

    if (config.keycloak.enable && !containers.contains(config.keycloak.externalName)) {
      startKeycloakContainer(config.keycloak)
    }
  }

  private fun startKeycloakContainer(config: ContainerConfig) {
    val container =
      KeycloakContainer("keycloak:20.0.2")
        .withRealmImportFile("keycloak-realm.json")
        .withReuse(true)
        .also { it.start() }
    System.setProperty("keycloak.auth-server-url", container.authServerUrl)
  }

  private fun startPostgresContainer(config: ContainerConfig) {
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

  private fun getContainers(output: String): List<String> =
    output
      .split("\n")
      .filter { row -> row.trim().isNotBlank() }
      .mapNotNull { row -> Regex("\\S+$").find(row)?.value }
}
