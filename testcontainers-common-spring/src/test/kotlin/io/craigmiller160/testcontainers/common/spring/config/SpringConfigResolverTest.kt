package io.craigmiller160.testcontainers.common.spring.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [SpringConfigResolver::class])
@ExtendWith(SpringExtension::class)
class SpringConfigResolverTest {
  @Autowired private lateinit var env: Environment

  companion object {
    @DynamicPropertySource
    @JvmStatic
    fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
      var count = 0
      registry.add("my-prop") {
        count++
        if (count % 2 == 0) {
          "hello"
        } else {
          "world"
        }
      }
    }
  }

  @Test
  fun `resolves configuration for all properties`() {
    assertEquals("world", env.getProperty("my-prop"))
    assertEquals("hello", env.getProperty("my-prop"))
  }

  @Test
  fun `resolves configuration for postgres only`() {
    TODO()
  }

  @Test
  fun `resolves configuration for keycloak only`() {
    TODO()
  }

  @Test
  fun `resolves configuration with no configuration provided`() {
    TODO()
  }
}
