package io.craigmiller160.testcontainers.common.core

import org.keycloak.admin.client.KeycloakBuilder

class AuthenticationHelper {
  companion object {
    const val ADMIN_CLIENT_ID = "admin-cli"
    const val CLIENT_GRANT_TYPE = "password"
  }

  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .username(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
      .password(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
      .clientId(ADMIN_CLIENT_ID)
      .grantType(CLIENT_GRANT_TYPE)
      .build()
}
