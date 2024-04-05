package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class TestcontainerInitializerTest {
  private lateinit var initResult: ContainerInitializationResult

  @AfterEach
  fun stopContainers() {
    initResult.keycloakContainer?.stop()
    initResult.mongoContainer?.stop()
    initResult.postgresContainer?.stop()
  }

  @Test
  fun `gets postgres schema name based on working directory`() {
    val schemaName = TestcontainerInitializer.getPostgresSchema()
    assertEquals("testcontainers_common", schemaName)
  }

  @Test
  fun `can initialize all containers`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true),
          keycloak = ContainerConfig(enable = true),
          mongo = ContainerConfig(enable = true)))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.STARTED)

    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_URL_PROP),
      result.postgresContainer?.jdbcUrl)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_R2_URL_PROP),
      result.postgresContainer?.jdbcUrl?.replace(Regex("^jdbc"), "r2dbc"))
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_USER_PROP),
      result.postgresContainer?.username)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP),
      result.postgresContainer?.password)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP),
      result.keycloakContainer?.authServerUrl?.replace(Regex("\\/$"), ""))
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_SCHEMA_PROP), "testcontainers_common")
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_REALM_PROP),
      TestcontainerConstants.KEYCLOAK_REALM)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_CLIENT_ID_PROP),
      TestcontainerConstants.KEYCLOAK_CLIENT_ID)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_CLIENT_SECRET_PROP),
      TestcontainerConstants.KEYCLOAK_CLIENT_SECRET)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_ADMIN_USER_PROP),
      TestcontainerConstants.KEYCLOAK_ADMIN_USER)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD_PROP),
      TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)

    TODO("Test for mongo")
  }

  @Test
  fun `can initialize postgres only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true), keycloak = null, mongo = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize keycloak only`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = null, keycloak = ContainerConfig(enable = true), mongo = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize mongo only`() {
    TODO()
  }

  @Test
  fun `can initialize nothing`() {
    val result =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = null, mongo = null))
    assertThat(result)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.DISABLED)
  }
}
