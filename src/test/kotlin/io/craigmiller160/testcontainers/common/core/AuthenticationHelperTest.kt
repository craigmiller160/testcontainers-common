package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AuthenticationHelperTest {
  companion object {
    @BeforeAll
    @JvmStatic
    fun setup() {
      TestcontainerInitializer.initialize(
        TestcontainersCommonConfig(postgres = null, keycloak = ContainerConfig(enable = true)))
    }
  }
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
  fun `can create a new role in keycloak`() {
    TODO()
  }

  @Test
  fun `will skip creating a new role in keycloak if the role already exists`() {
    TODO()
  }
}
