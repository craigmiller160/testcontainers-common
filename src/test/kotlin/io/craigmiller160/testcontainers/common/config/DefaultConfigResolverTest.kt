package io.craigmiller160.testcontainers.common.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DefaultConfigResolverTest {
  @Test
  fun `resolves configuration for all properties`() {
    val resolver = DefaultConfigResolver()
    val config = resolver.resolve()
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
  fun `resolves configuration with no configuration provided`() {
    TODO()
  }
}
