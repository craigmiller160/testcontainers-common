package us.craigmiller160.testcontainers.common.spring

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import us.craigmiller160.testcontainers.common.core.TestcontainerInitializer
import us.craigmiller160.testcontainers.common.spring.config.SpringConfigResolver

class SpringTestContainersExtension : BeforeAllCallback {
  override fun beforeAll(context: ExtensionContext) {
    SpringExtension.getApplicationContext(context)
        .let { SpringConfigResolver(it.environment).resolve() }
        .let { TestcontainerInitializer.initialize(it) }
  }
}
