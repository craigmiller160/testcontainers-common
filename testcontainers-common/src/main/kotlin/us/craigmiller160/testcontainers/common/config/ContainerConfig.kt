package io.craigmiller160.testcontainers.common.config

data class ContainerConfig(val enable: Boolean) {
  constructor(map: Map<String, Any>) : this(map["enable"] as Boolean)
}
