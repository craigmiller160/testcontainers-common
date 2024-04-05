package io.craigmiller160.testcontainers.common.core

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

data class ContainerInitializationResult(
    val postgresStatus: ContainerStatus,
    val keycloakStatus: ContainerStatus,
    val mongoStatus: ContainerStatus,
    val postgresContainer: PostgreSQLContainer<*>?,
    val keycloakContainer: KeycloakContainer?,
    val mongoContainer: MongoDBContainer?
)
