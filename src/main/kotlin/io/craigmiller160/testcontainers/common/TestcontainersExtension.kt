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
    val imageTag = config.overrideImageTag ?: "20.0.2"
    val container = KeycloakContainer("keycloak:$imageTag")
  }

  private fun startPostgresContainer(config: ContainerConfig) {
    val imageTag = config.overrideImageTag ?: "14.5"
    val container = PostgreSQLContainer("postgres:${config.overrideImageTag}")
  }

  private fun getContainers(output: String): List<String> =
    output
      .split("\n")
      .filter { row -> row.trim().isNotBlank() }
      .mapNotNull { row -> Regex("\\S+$").find(row)?.value }
}
