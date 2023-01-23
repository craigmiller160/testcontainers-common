package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AuthenticationHelperTest {
  companion object {
    @BeforeAll
    @JvmStatic
    fun setup() {
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = ContainerConfig(enable = true)))
    }
  }
  @Test
  fun `can create a user in keycloak and then login with that user`() {
    TODO()
  }
}
