package us.craigmiller160.testcontainers.common.spring

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import us.craigmiller160.testcontainers.common.core.TestcontainerInitializer
import us.craigmiller160.testcontainers.common.spring.config.SpringConfigResolver

@Configuration
@Profile("test")
class SpringTestContainersAutoConfiguration(private val env: Environment) {
  @PostConstruct
  fun init() {
    SpringConfigResolver(env).resolve().let { TestcontainerInitializer.initialize(it) }
  }
}
