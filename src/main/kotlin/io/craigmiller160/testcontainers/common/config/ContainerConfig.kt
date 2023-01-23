package io.craigmiller160.testcontainers.common.config

data class ContainerConfig(
  val enable: Boolean,
  val propertyMappings: Map<String, String> = mapOf()
) {
  constructor(
    map: Map<String, Any>
  ) : this(map["enable"] as Boolean, map["propertyMappings"] as Map<String, String>? ?: mapOf())
}
