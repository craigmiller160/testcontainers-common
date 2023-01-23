package io.craigmiller160.testcontainers.common.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.util.Base64
import java.util.UUID
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.keycloak.admin.client.KeycloakBuilder

class AuthenticationHelper {
  companion object {
    const val ADMIN_CLIENT_ID = "admin-cli"
    const val CLIENT_ID = "test-client"
    const val CLIENT_SECRET = "CQ60kzXcV7F31JfW8dX2MINjVIKOIxpJ"
    const val GRANT_TYPE_KEY = "grant_type"
    const val GRANT_TYPE_VALUE = "password"
    const val CLIENT_ID_KEY = "client_id"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
  }

  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .username(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
      .password(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
      .clientId(ADMIN_CLIENT_ID)
      .grantType(GRANT_TYPE_VALUE)
      .build()
  private val httpClient = HttpClients.createDefault()
  private val objectMapper = jacksonObjectMapper()

  fun login(userName: String, password: String): String {
    val entity =
      UrlEncodedFormEntity(
        listOf(
          BasicNameValuePair(GRANT_TYPE_KEY, GRANT_TYPE_VALUE),
          BasicNameValuePair(CLIENT_ID_KEY, CLIENT_ID),
          BasicNameValuePair(USERNAME_KEY, userName),
          BasicNameValuePair(PASSWORD_KEY, password)))

    val basicAuth =
      "Basic ${Base64.getEncoder().encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray())}"
    val httpPost =
      HttpPost().apply {
        addHeader("Authorization", basicAuth)
        this.entity = entity
      }

    return httpClient.execute(httpPost).use { response ->
      val tokenType = jacksonTypeRef<Map<String, Any>>()
      val tokenResponse = objectMapper.readValue(response.entity.content, tokenType)
      tokenResponse["access_token"] as String
    }
  }

  data class TestUser(
    val userId: UUID,
    val userName: String,
    val roles: List<String>,
    val token: String
  )
}
