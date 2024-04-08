package io.craigmiller160.testcontainers.common.config

data class TestcontainersCommonConfig(
    val postgres: ContainerConfig?,
    val keycloak: ContainerConfig?,
    val mongo: ContainerConfig?
) {
  constructor(
      map: Map<String, Any>
  ) : this(
      map["postgres"]?.let { ContainerConfig(it as Map<String, Any>) },
      map["keycloak"]?.let { ContainerConfig(it as Map<String, Any>) },
      map["mongodb"]?.let { ContainerConfig(it as Map<String, Any>) })
}
