package io.craigmiller160.testcontainers.common.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.URI
import java.util.Base64
import java.util.UUID
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation

class AuthenticationHelper {
  companion object {
    internal const val ADMIN_CLIENT_ID = "admin-cli"
    internal const val CLIENT_ID_KEY = "client_id"
    internal const val GRANT_TYPE_KEY = "grant_type"
    internal const val GRANT_TYPE_VALUE = "password"
    internal const val USERNAME_KEY = "username"
    internal const val PASSWORD_KEY = "password"
    internal const val ADMIN_REALM = "master"

    const val ACCESS_ROLE = "access"
    const val DEFAULT_PASSWORD = "password"
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

  @JvmOverloads
  fun createUser(userName: String, roles: List<String> = listOf(ACCESS_ROLE)): TestUser {
    val realUserName = "${UUID.randomUUID()}_$userName"
    val realm = keycloak.realm(TestcontainerConstants.KEYCLOAK_REALM)
    val kcClientId =
      realm.clients().findByClientId(TestcontainerConstants.KEYCLOAK_CLIENT_ID).first().id
    val client = realm.clients().get(kcClientId)
    val accessRole = client.roles().get(ACCESS_ROLE).toRepresentation()
    val users = realm.users()

    val userId =
      UserRepresentation()
        .apply {
          username = realUserName
          isEmailVerified = true
          isEnabled = true
          firstName = "First $realUserName"
          lastName = "Last $realUserName"
          email = realUserName
        }
        .let { users.create(it) }
        .let { CreatedResponseUtil.getCreatedId(it) }

    CredentialRepresentation()
      .apply {
        isTemporary = false
        type = CredentialRepresentation.PASSWORD
        value = DEFAULT_PASSWORD
      }
      .let { users.get(userId).resetPassword(it) }

    roles
      .map { role -> client.roles().get(role).toRepresentation() }
      .let { users.get(userId).roles().clientLevel(kcClientId).add(it) }

    return TestUser(userId = UUID.fromString(userId), userName = realUserName, roles = roles)
  }

  fun createRole(roleName: String) {
    val realm = keycloak.realm(TestcontainerConstants.KEYCLOAK_REALM)
    val kcClientId =
      realm.clients().findByClientId(TestcontainerConstants.KEYCLOAK_CLIENT_ID).first().id
    val client = realm.clients().get(kcClientId)

    val role = RoleRepresentation().apply { name = roleName }

    client.roles().create(role)
  }

  fun login(testUser: TestUser): TestUserWithToken {
    val clientId = TestcontainerConstants.KEYCLOAK_CLIENT_ID
    val clientSecret = TestcontainerConstants.KEYCLOAK_CLIENT_SECRET
    val entity =
      UrlEncodedFormEntity(
        listOf(
          BasicNameValuePair(GRANT_TYPE_KEY, GRANT_TYPE_VALUE),
          BasicNameValuePair(CLIENT_ID_KEY, clientId),
          BasicNameValuePair(USERNAME_KEY, testUser.userName),
          BasicNameValuePair(PASSWORD_KEY, DEFAULT_PASSWORD)))

    val basicAuth =
      "Basic ${Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())}"
    val httpPost =
      HttpPost().apply {
        uri =
          URI.create(
            "${System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP)}/realms/apps-dev/protocol/openid-connect/token")
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

  data class TestUser(val userId: UUID, val userName: String, val roles: List<String>)

  data class TestUserWithToken(
    val userId: UUID,
    val userName: String,
    val roles: List<String>,
    val token: String
  ) {
    companion object {
      fun fromTestUser(testUser: TestUser, token: String): TestUserWithToken =
        TestUserWithToken(
          userId = testUser.userId,
          userName = testUser.userName,
          roles = testUser.roles,
          token = token)
    }
  }
}
