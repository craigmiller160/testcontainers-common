package io.craigmiller160.testcontainers.common.spring.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.env.MockEnvironment

class SpringConfigResolverTest {

  @Test
  fun `resolves configuration for all containers`() {
    val env =
        MockEnvironment()
            .withProperty("testcontainers.common.postgres.enable", "true")
            .withProperty("testcontainers.common.keycloak.enable", "true")
            .withProperty("testcontainers.common.mongodb.enable", "true")
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).isNotNull.hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.postgres).isNotNull.hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).isNotNull.hasFieldOrPropertyWithValue("enable", true)
  }

  @Test
  fun `resolves configuration for postgres only`() {
    TODO()
  }

  @Test
  fun `resolves configuration for keycloak only`() {
    TODO()
  }

  @Test
  fun `resolves configuration for mongo only`() {
    TODO()
  }

  @Test
  fun `resolves configuration with no configuration provided`() {
    TODO()
  }
}
