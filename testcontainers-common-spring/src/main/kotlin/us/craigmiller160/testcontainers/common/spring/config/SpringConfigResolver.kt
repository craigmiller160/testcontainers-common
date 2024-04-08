package io.craigmiller160.testcontainers.common.spring.config

import io.craigmiller160.testcontainers.common.config.ConfigResolver
import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.springframework.core.env.Environment

class SpringConfigResolver(private val environment: Environment) : ConfigResolver {
  override fun resolve(): TestcontainersCommonConfig {
    val postgresConfig =
        environment.getProperty("testcontainers.common.postgres.enable")?.let {
          ContainerConfig(it == "true")
        }
    val keycloakConfig =
        environment.getProperty("testcontainers.common.keycloak.enable")?.let {
          ContainerConfig(it == "true")
        }
    val mongoConfig =
        environment.getProperty("testcontainers.common.mongodb.enable")?.let {
          ContainerConfig(it == "true")
        }
    return TestcontainersCommonConfig(
        postgres = postgresConfig, keycloak = keycloakConfig, mongo = mongoConfig)
  }
}
