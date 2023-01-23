package io.craigmiller160.testcontainers.common.core

import io.craigmiller160.testcontainers.common.config.ContainerConfig
import io.craigmiller160.testcontainers.common.config.TestcontainersCommonConfig
import java.util.UUID
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
    val id = UUID.randomUUID().toString()
    val userName = "MyUser_$id@gmail.com"
    val testUser = helper.createUser(userName)
    assertThat(testUser)
      .hasFieldOrPropertyWithValue("userName", userName)
      .hasFieldOrPropertyWithValue("roles", listOf(AuthenticationHelper.ACCESS_ROLE))

    val token = helper.login(testUser)
    assertNotNull(token)
  }
}
