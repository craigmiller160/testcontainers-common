package io.craigmiller160.testcontainers.common.config

data class TestcontainersCommonConfig(
  val postgres: ContainerConfig?,
  val keycloak: ContainerConfig?
) {
  constructor(map: Map<String, Any>) : this(null, null)
}
