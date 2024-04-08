package us.craigmiller160.testcontainers.common.config

data class TestcontainersCommonConfig(
    val postgres: ContainerConfig,
    val keycloak: ContainerConfig,
    val mongo: ContainerConfig
) {
  constructor(
      map: Map<String, Any>
  ) : this(
      map.getContainerConfig("postgres"),
      map.getContainerConfig("keycloak"),
      map.getContainerConfig("mongo"))
}

private fun Map<String, Any>.getContainerConfig(key: String): ContainerConfig =
    this[key]?.let { value ->
      if (value !is Map<*, *>) {
        throw IllegalArgumentException("testcontainers config value for $key is not of type Map")
      }
      ContainerConfig(value as Map<String, Any>)
    } ?: ContainerConfig(enable = false)
