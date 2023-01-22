package io.craigmiller160.testcontainers.common.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DefaultConfigResolverTest {
  @Test
  fun `resolves configuration`() {
    // TODO need to test different scenarios
    val resolver = DefaultConfigResolver()
    val config = resolver.resolve()
    assertThat(config.postgres).isNotNull.hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).isNotNull.hasFieldOrPropertyWithValue("enable", true)
  }
}
