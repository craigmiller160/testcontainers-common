package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.junit.jupiter.api.Test

class TestcontainerInitializerTest {
  private val config =
    TestcontainersCommonConfig(
      postgres = ContainerConfig(enable = true), keycloak = ContainerConfig(enable = true))

  @Test
  fun `can initialize containers`() {
    TestcontainerInitializer.initialize(config)
  }
}
