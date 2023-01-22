package io.craigmiller160.testcontainers.common.config

data class TestcontainersCommonConfig(
  val postgres: ContainerConfig?,
  val keycloak: ContainerConfig?
)
