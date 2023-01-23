package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import kotlin.test.assertEquals
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
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)

    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_URL_PROP),
      result.postgresContainer?.jdbcUrl)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_USER_PROP),
      result.postgresContainer?.username)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP),
      result.postgresContainer?.password)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP),
      result.keycloakContainer?.authServerUrl)
  }

  @Test
  fun `can initialize postgres only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = ContainerConfig(enable = true), keycloak = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize keycloak only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = ContainerConfig(enable = true)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)
  }

  @Test
  fun `can initialize nothing`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize all containers and re-map properties`() {
    val postgresMap =
      mapOf(
        TestcontainerConstants.POSTGRES_URL_PROP to "spring.datasource.url",
        TestcontainerConstants.POSTGRES_USER_PROP to "spring.datsource.username",
        TestcontainerConstants.POSTGRES_PASSWORD_PROP to "spring.datasource.password")
    val keycloakMap = mapOf(TestcontainerConstants.KEYCLOAK_URL_PROP to "keycloak.auth-server-url")
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true, postgresMap),
          keycloak = ContainerConfig(enable = true, keycloakMap)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)

    assertEquals(System.getProperty("spring.datasource.url"), result.postgresContainer?.jdbcUrl)
    assertEquals(
      System.getProperty("spring.datsource.username"), result.postgresContainer?.username)
    assertEquals(
      System.getProperty("spring.datasource.password"), result.postgresContainer?.password)
    assertEquals(
      System.getProperty("keycloak.auth-server-url"), result.keycloakContainer?.authServerUrl)
  }
}
