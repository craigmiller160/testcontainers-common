package io.craigmiller160.testcontainers.common.config

import org.junit.jupiter.api.Test

class DefaultConfigResolverTest {
  @Test
  fun `resolves configuration`() {
    val resolver = DefaultConfigResolver()
    val config = resolver.resolve()
  }
}
