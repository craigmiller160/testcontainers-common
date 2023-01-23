package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestcontainerInitializerTest {
  private val config =
    TestcontainersCommonConfig(
      postgres = ContainerConfig(enable = true), keycloak = ContainerConfig(enable = true))

  @Test
  fun `can initialize all containers`() {
    val result = TestcontainerInitializer.initialize(config)
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.STARTED)
  }

  @Test
  fun `can initialize postgres only`() {
    TODO()
  }

  @Test
  fun `can initialize keycloak only`() {
    TODO()
  }

  @Test
  fun `can initialize nothing`() {
    TODO()
  }

  @Test
  fun `can initialize all containers and re-map properties`() {
    TODO()
  }
}
