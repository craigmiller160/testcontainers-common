package us.craigmiller160.testcontainers.common.spring.config

import org.springframework.core.env.Environment
import us.craigmiller160.testcontainers.common.config.ConfigResolver
import us.craigmiller160.testcontainers.common.config.ContainerConfig
import us.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig

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
