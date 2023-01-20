package io.craigmiller160.testcontainers.common.config

data class ContainerConfig(
  val enable: Boolean,
  val overrideImageTag: String? = null,
  val externalName: String? = null
)
