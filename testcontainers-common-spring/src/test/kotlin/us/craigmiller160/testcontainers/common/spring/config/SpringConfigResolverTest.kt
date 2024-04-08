package us.craigmiller160.testcontainers.common.spring.config

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
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", true)
  }

  @Test
  fun `resolves configuration for postgres only`() {
    val env =
        MockEnvironment()
            .withProperty("testcontainers.common.postgres.enable", "true")
            .withProperty("testcontainers.common.keycloak.enable", "false")
            .withProperty("testcontainers.common.mongodb.enable", "false")
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", false)
  }

  @Test
  fun `resolves configuration for keycloak only`() {
    val env =
        MockEnvironment()
            .withProperty("testcontainers.common.postgres.enable", "false")
            .withProperty("testcontainers.common.keycloak.enable", "true")
            .withProperty("testcontainers.common.mongodb.enable", "false")
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", true)
  }

  @Test
  fun `resolves configuration for mongo only`() {
    val env =
        MockEnvironment()
            .withProperty("testcontainers.common.postgres.enable", "false")
            .withProperty("testcontainers.common.keycloak.enable", "false")
            .withProperty("testcontainers.common.mongodb.enable", "true")
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", true)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", false)
  }

  @Test
  fun `resolves configuration for none of them`() {
    val env =
        MockEnvironment()
            .withProperty("testcontainers.common.postgres.enable", "false")
            .withProperty("testcontainers.common.keycloak.enable", "false")
            .withProperty("testcontainers.common.mongodb.enable", "false")
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", false)
  }

  @Test
  fun `resolves configuration with no configuration provided`() {
    val env = MockEnvironment()
    val config = SpringConfigResolver(env).resolve()
    assertThat(config.mongo).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.postgres).hasFieldOrPropertyWithValue("enable", false)
    assertThat(config.keycloak).hasFieldOrPropertyWithValue("enable", false)
  }
}
