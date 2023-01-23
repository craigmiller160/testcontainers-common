package io.craigmiller160.testcontainers.common.core

import java.util.UUID
import org.keycloak.admin.client.KeycloakBuilder

class AuthenticationHelper {
  companion object {
    const val ADMIN_CLIENT_ID = "admin-cli"
    const val CLI_GRANT_TYPE = "password"
    const val CLIENT_ID = "test-client"
    const val CLIENT_SECRET = "CQ60kzXcV7F31JfW8dX2MINjVIKOIxpJ"
  }

  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .username(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
      .password(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
      .clientId(ADMIN_CLIENT_ID)
      .grantType(CLIENT_GRANT_TYPE)
      .build()

  fun login(userName: String, password: String): String {}

  data class TestUser(
    val userId: UUID,
    val userName: String,
    val roles: List<String>,
    val token: String
  )
}
