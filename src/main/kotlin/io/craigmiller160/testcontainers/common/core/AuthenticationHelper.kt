package io.craigmiller160.testcontainers.common.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.util.Base64
import java.util.UUID
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation

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
    const val ACCESS_ROLE = "access"
    const val ADMIN_REALM = "master"
    const val REALM = "apps-dev"
  }

  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .realm(ADMIN_REALM)
      .username(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
      .password(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
      .clientId(ADMIN_CLIENT_ID)
      .grantType(GRANT_TYPE_VALUE)
      .build()
  private val httpClient = HttpClients.createDefault()
  private val objectMapper = jacksonObjectMapper()

  fun createUser(
    userName: String,
    password: String,
    roles: List<String> = listOf(ACCESS_ROLE)
  ): TestUser {
    val realm = keycloak.realm(REALM)
    val kcClientId = realm.clients().findByClientId(CLIENT_ID).first().id
    val client = realm.clients().get(kcClientId)
    val accessRole = client.roles().get(ACCESS_ROLE).toRepresentation()
    val users = realm.users()

    val userId =
      UserRepresentation()
        .apply {
          username = userName
          isEmailVerified = true
          isEnabled = true
          firstName = "First $userName"
          lastName = "Last $userName"
          email = userName
        }
        .let { users.create(it) }
        .let { CreatedResponseUtil.getCreatedId(it) }

    CredentialRepresentation()
      .apply {
        isTemporary = false
        type = CredentialRepresentation.PASSWORD
        value = password
      }
      .let { users.get(userId).resetPassword(it) }

    roles
      .map { role -> client.roles().get(role).toRepresentation() }
      .let { users.get(userId).roles().clientLevel(kcClientId).add(it) }

    return TestUser(
      userId = UUID.fromString(userId), userName = userName, password = password, roles = roles)
  }

  fun login(testUser: TestUser): TestUserWithToken {
    val entity =
      UrlEncodedFormEntity(
        listOf(
          BasicNameValuePair(GRANT_TYPE_KEY, GRANT_TYPE_VALUE),
          BasicNameValuePair(CLIENT_ID_KEY, CLIENT_ID),
          BasicNameValuePair(USERNAME_KEY, testUser.userName),
          BasicNameValuePair(PASSWORD_KEY, testUser.password)))

    val basicAuth =
      "Basic ${Base64.getEncoder().encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray())}"
    val httpPost =
      HttpPost().apply {
        addHeader("Authorization", basicAuth)
        this.entity = entity
      }

    val token =
      httpClient.execute(httpPost).use { response ->
        val tokenType = jacksonTypeRef<Map<String, Any>>()
        val tokenResponse = objectMapper.readValue(response.entity.content, tokenType)
        tokenResponse["access_token"] as String
      }
    return TestUserWithToken.fromTestUser(testUser, token)
  }

  data class TestUser(
    val userId: UUID,
    val userName: String,
    val password: String,
    val roles: List<String>
  )

  data class TestUserWithToken(
    val userId: UUID,
    val userName: String,
    val password: String,
    val roles: List<String>,
    val token: String
  ) {
    companion object {
      fun fromTestUser(testUser: TestUser, token: String): TestUserWithToken =
        TestUserWithToken(
          userId = testUser.userId,
          userName = testUser.userName,
          password = testUser.password,
          roles = testUser.roles,
          token = token)
    }
  }
}
