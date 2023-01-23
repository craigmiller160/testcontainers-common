package io.craigmiller160.testcontainers.common.core

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.testcontainers.containers.PostgreSQLContainer

data class ContainerInitializationResult(
  val postgresStatus: ContainerStatus,
  val keycloak: ContainerStatus,
  val postgresContainer: PostgreSQLContainer<*>?,
  val keycloakContainer: KeycloakContainer?
)
