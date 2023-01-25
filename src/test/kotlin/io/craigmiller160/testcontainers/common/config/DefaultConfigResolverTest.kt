package io.craigmiller160.testcontainers.common.config

import java.lang.NullPointerException
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefaultConfigResolverTest {
  @Test
  fun `resolves configuration for all properties`() {
    val resolver = DefaultConfigResolver("configResolverTest/all-props.yml")
    val config = resolver.resolve()
    assertThat(config.postgres).isNotNull.hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).isNotNull.hasFieldOrPropertyWithValue("enable", true)
  }

  @Test
  fun `resolves configuration for postgres only`() {
    val resolver = DefaultConfigResolver("configResolverTest/postgres-only.yml")
    val config = resolver.resolve()
    assertThat(config.postgres).isNotNull.hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).isNull()
  }

  @Test
  fun `resolves configuration for keycloak only`() {
    val resolver = DefaultConfigResolver("configResolverTest/keycloak-only.yml")
    val config = resolver.resolve()
    assertThat(config.postgres).isNull()
    assertThat(config.keycloak).isNotNull.hasFieldOrPropertyWithValue("enable", true)
  }

  @Test
  fun `resolves configuration with no configuration in file`() {
    val resolver = DefaultConfigResolver("configResolverTest/nothing.yml")
    val config = resolver.resolve()
    assertThat(config.postgres).isNull()
    assertThat(config.keycloak).isNull()
  }

  @Test
  fun `resolves configuration without file present`() {
    val resolver = DefaultConfigResolver("configResolverTest/foobar.yml")
    val ex = assertThrows<NullPointerException> { resolver.resolve() }
    assertEquals("No configResolverTest/foobar.yml file found at root of classpath", ex.message)
  }
}
