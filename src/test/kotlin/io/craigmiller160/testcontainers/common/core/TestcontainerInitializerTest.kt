package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestcontainerInitializerTest {

  @Test
  fun `can initialize all containers`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true), keycloak = ContainerConfig(enable = true)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.STARTED)
    TODO("Validate the container settings")
  }

  @Test
  fun `can initialize postgres only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = ContainerConfig(enable = true), keycloak = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize keycloak only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = ContainerConfig(enable = true)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.STARTED)
  }

  @Test
  fun `can initialize nothing`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize all containers and re-map properties`() {
    val postgresMap =
      mapOf(
        "postgres.url" to "spring.datasource.url",
        "postgres.username" to "spring.datsource.username",
        "postgres.password" to "spring.datasource.password")
    val keycloakMap = mapOf("keycloak.url" to "keycloak.auth-server-url")
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true, postgresMap),
          keycloak = ContainerConfig(enable = true, keycloakMap)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgres", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloak", ContainerStatus.STARTED)
    TODO("Validate the container settings")
  }
}
