package io.craigmiller160.testcontainers.common.core

data class ContainerInitializationResult(
  val postgres: ContainerStatus,
  val keycloak: ContainerStatus
)
