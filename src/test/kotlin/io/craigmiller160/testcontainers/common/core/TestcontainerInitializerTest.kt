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

  private fun validatePostgresProps() {
    assertThat(System.getProperty(TestcontainerConstants.POSTGRES_URL_PROP))
      .isEqualTo(initResult?.postgresContainer?.jdbcUrl)
    assertThat(System.getProperty(TestcontainerConstants.POSTGRES_R2_URL_PROP))
      .isEqualTo(initResult?.postgresContainer?.jdbcUrl?.replace(Regex("^jdbc"), "r2dbc"))
    assertThat(System.getProperty(TestcontainerConstants.POSTGRES_USER_PROP))
      .isEqualTo(initResult?.postgresContainer?.username)
    assertThat(System.getProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP))
      .isEqualTo(initResult?.postgresContainer?.password)
  }

  private fun validateKeycloakProps() {
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .isEqualTo(initResult?.keycloakContainer?.authServerUrl?.replace(Regex("\\/$"), ""))
    assertThat(System.getProperty(TestcontainerConstants.POSTGRES_SCHEMA_PROP))
      .isEqualTo("testcontainers_common")
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_REALM_PROP))
      .isEqualTo(TestcontainerConstants.KEYCLOAK_REALM)
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_CLIENT_ID_PROP))
      .isEqualTo(TestcontainerConstants.KEYCLOAK_CLIENT_ID)
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_CLIENT_SECRET_PROP))
      .isEqualTo(TestcontainerConstants.KEYCLOAK_CLIENT_SECRET)
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_ADMIN_USER_PROP))
      .isEqualTo(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
    assertThat(System.getProperty(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD_PROP))
      .isEqualTo(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
  }

  private fun validateMongoProps() {
    assertThat(System.getProperty(TestcontainerConstants.MONGO_URL_PROP))
      .isEqualTo("mongodb://localhost:27018/test")
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

    validatePostgresProps()
    validateKeycloakProps()
    validateMongoProps()
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

    validatePostgresProps()
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

    validateKeycloakProps()
  }

  @Test
  fun `can initialize mongo only`() {
    initResult =
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(
          postgres = null, keycloak = null, mongo = ContainerConfig(enable = true)))
    assertThat(initResult)
      .hasFieldOrPropertyWithValue("postgresStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("keycloakStatus", ContainerStatus.DISABLED)
      .hasFieldOrPropertyWithValue("mongoStatus", ContainerStatus.STARTED)

    validateMongoProps()
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
