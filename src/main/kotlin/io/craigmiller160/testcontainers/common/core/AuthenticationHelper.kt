package io.craigmiller160.testcontainers.common.core

import org.keycloak.admin.client.KeycloakBuilder

class AuthenticationHelper {
  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .build()
}
