package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class TestcontainerInitializerTest {
  private var initResult: ContainerInitializationResult? = null

  @AfterEach
  fun stopContainers() {
    initResult?.keycloakContainer?.stop()
    initResult?.mongoContainer?.stop()
    initResult?.postgresContainer?.stop()
    initResult = null
  }

  @Test
  fun `gets postgres schema name based on working directory`() {
    val schemaName = TestcontainerInitializer.getPostgresSchema()
    assertEquals("testcontainers_common", schemaName)
  }

  @Test
  fun `can initialize all containers`() {
    initResult =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true),
          keycloak = ContainerConfig(enable = true),
          mongo = ContainerConfig(enable = true)))
    assertThat(initResult)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.STARTED)

    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_URL_PROP),
      initResult?.postgresContainer?.jdbcUrl)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_R2_URL_PROP),
      initResult?.postgresContainer?.jdbcUrl?.replace(Regex("^jdbc"), "r2dbc"))
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_USER_PROP),
      initResult?.postgresContainer?.username)
    assertEquals(
      System.getProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP),
      initResult?.postgresContainer?.password)
    assertEquals(
      System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP),
      initResult?.keycloakContainer?.authServerUrl?.replace(Regex("\\/$"), ""))
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
    initResult =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = ContainerConfig(enable = true), keycloak = null, mongo = null))
    assertThat(initResult)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.STARTED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.DISABLED)
  }

  @Test
  fun `can initialize keycloak only`() {
    initResult =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = null, keycloak = ContainerConfig(enable = true), mongo = null))
    assertThat(initResult)
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
    initResult =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = null, mongo = null))
    assertThat(initResult)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.DISABLED)
  }
}
