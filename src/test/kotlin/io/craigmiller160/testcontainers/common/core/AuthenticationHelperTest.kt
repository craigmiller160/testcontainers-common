package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import java.util.UUID
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.RoleRepresentation

class AuthenticationHelperTest {
  companion object {
    @BeforeAll
    @JvmStatic
    fun setup() {
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = ContainerConfig(enable = true)))
    }
  }

  private val keycloak =
    KeycloakBuilder.builder()
      .serverUrl(System.getProperty(TestcontainerConstants.KEYCLOAK_URL_PROP))
      .realm(AuthenticationHelper.ADMIN_REALM)
      .username(TestcontainerConstants.KEYCLOAK_ADMIN_USER)
      .password(TestcontainerConstants.KEYCLOAK_ADMIN_PASSWORD)
      .clientId(AuthenticationHelper.ADMIN_CLIENT_ID)
      .grantType(AuthenticationHelper.GRANT_TYPE_VALUE)
      .build()

  @Test
  fun `can create a user in keycloak and then login with that user`() {
    val helper = AuthenticationHelper()
    val userName = "MyUser@gmail.com"
    val testUser = helper.createUser(userName)
    assertThat(testUser)
      .hasFieldOrPropertyWithValue("roles", listOf(AuthenticationHelper.ACCESS_ROLE))
    assertThat(testUser.userName).matches("^\\S+_$userName")

    val token = helper.login(testUser)
    assertNotNull(token)
  }

  @Test
  fun `can create a user in keycloak with additional roles`() {
    val helper = AuthenticationHelper()
    val userName = "MyUser@gmail.com"
    val roleName = "OtherRole_${UUID.randomUUID()}"

    val realm = keycloak.realm(AuthenticationHelper.ADMIN_REALM)
    val kcClientId =
      realm.clients().findByClientId(TestcontainerConstants.KEYCLOAK_CLIENT_ID).first().id
    realm.clients().get(kcClientId).roles().create(RoleRepresentation().apply { name = roleName })

    val testUser = helper.createUser(userName, listOf(AuthenticationHelper.ACCESS_ROLE, roleName))
    assertThat(testUser)
      .hasFieldOrPropertyWithValue("roles", listOf(AuthenticationHelper.ACCESS_ROLE))
    assertThat(testUser.userName).matches("^\\S+_$userName")

    val roles =
      keycloak
        .realm(AuthenticationHelper.ADMIN_REALM)
        .users()
        .get(testUser.userId.toString())
        .roles()
        .clientLevel(kcClientId)
        .listAll()
    println(roles) // TODO delete this
  }

  @Test
  fun `can create a new role in keycloak`() {
    TODO()
  }

  @Test
  fun `will skip creating a new role in keycloak if the role already exists`() {
    TODO()
  }
}
