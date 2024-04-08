package io.craigmiller160.testcontainers.common.core

import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import dasniko.testcontainers.keycloak.KeycloakContainer
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import io.craigmiller160.testcontainers.common.utils.Terminal
import java.nio.file.Paths
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer

object TestcontainerInitializer {
  fun initialize(config: TestcontainersCommonConfig): ContainerInitializationResult {
    val (postgresStatus, postgresContainer) =
        if (config.postgres?.enable == true) {
          startPostgresContainer()
        } else {
          ContainerStatus.DISABLED to null
        }

    val (keycloakStatus, keycloakContainer) =
        if (config.keycloak?.enable == true) {
          startKeycloakContainer()
        } else {
          ContainerStatus.DISABLED to null
        }

    val (mongoStatus, mongoContainer) =
        if (config.mongo?.enable == true) {
          startMongoContainer()
        } else {
          ContainerStatus.DISABLED to null
        }

    return ContainerInitializationResult(
        postgresStatus = postgresStatus,
        keycloakStatus = keycloakStatus,
        mongoStatus = mongoStatus,
        postgresContainer = postgresContainer,
        keycloakContainer = keycloakContainer,
        mongoContainer = mongoContainer)
  }

  private fun startMongoContainer(): Pair<ContainerStatus, MongoDBContainer> {
    val container =
        MongoDBContainer(TestcontainerConstants.MONGO_IMAGE)
            .withExposedPorts(27017)
            .withCreateContainerCmdModifier { cmd -> cmd.bindToPublicPort(27018, 27017) }
            .also { it.start() }
    System.setProperty(TestcontainerConstants.MONGO_URL_PROP, container.replicaSetUrl)

    return ContainerStatus.STARTED to container
  }

  private fun startKeycloakContainer(): Pair<ContainerStatus, KeycloakContainer> {
    val container =
        KeycloakContainer(TestcontainerConstants.KEYCLOAK_IMAGE)
            .withAdminUsername(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
            .withAdminPassword(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
            .withRealmImportFile(TestcontainerConstants.KEYCLOAK_REALM_FILE)
            .withReuse(true)
            .withExposedPorts(8080)
            .withCreateContainerCmdModifier { cmd -> cmd.bindToPublicPort(8081, 8080) }
            .also { it.start() }
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_URL_PROP,
        container.authServerUrl.replace(Regex("\\/$"), ""))
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_REALM_PROP, TestcontainerConstants.KEYCLOAK_REALM)
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_CLIENT_ID_PROP, TestcontainerConstants.KEYCLOAK_CLIENT_ID)
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_CLIENT_SECRET_PROP,
        TestcontainerConstants.KEYCLOAK_CLIENT_SECRET)
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_ADMIN_USER_PROP, TestcontainerConstants.KEYCLOAK_ADMIN_USER)
    System.setProperty(
        TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD_PROP,
        TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
    return ContainerStatus.STARTED to container
  }

  fun getPostgresSchema(): String =
      Paths.get(Terminal.execute("pwd")).fileName.toString().trim().replace("-", "_")

  private fun initializeSchema(container: PostgreSQLContainer<*>, schemaName: String) {
    container.createConnection("").use { conn ->
      conn.createStatement().execute("CREATE SCHEMA IF NOT EXISTS $schemaName")
    }
  }

  private fun startPostgresContainer(): Pair<ContainerStatus, PostgreSQLContainer<*>> {
    val container =
        PostgreSQLContainer(TestcontainerConstants.POSTGRES_IMAGE)
            .withUsername(TestcontainerConstants.POSTGRES_USER)
            .withPassword(TestcontainerConstants.POSTGRES_PASSWORD)
            .withDatabaseName(TestcontainerConstants.POSTGRES_DB_NAME)
            .withReuse(true)
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier { cmd -> cmd.bindToPublicPort(5433, 5432) }
            .also { it.start() }
    val schemaName = getPostgresSchema()
    initializeSchema(container, schemaName)
    System.setProperty(TestcontainerConstants.POSTGRES_URL_PROP, container.jdbcUrl)
    System.setProperty(
        TestcontainerConstants.POSTGRES_R2_URL_PROP,
        container.jdbcUrl.replace(Regex("^jdbc"), "r2dbc"))
    System.setProperty(TestcontainerConstants.POSTGRES_PASSWORD_PROP, container.password)
    System.setProperty(TestcontainerConstants.POSTGRES_USER_PROP, container.username)
    System.setProperty(TestcontainerConstants.POSTGRES_SCHEMA_PROP, schemaName)
    return ContainerStatus.STARTED to container
  }

  private fun CreateContainerCmd.bindToPublicPort(
      publicPort: Int,
      privatePort: Int
  ): CreateContainerCmd =
      this.withHostConfig(
          HostConfig()
              .withPortBindings(
                  PortBinding(Ports.Binding.bindPort(publicPort), ExposedPort(privatePort))))
}
