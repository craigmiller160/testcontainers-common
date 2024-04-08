package us.craigmiller160.testcontainers.common

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import us.craigmiller160.testcontainers.common.config.DefaultConfigResolver
import us.craigmiller160.testcontainers.common.core.TestcontainerInitializer

class TestcontainersExtension : BeforeAllCallback {
  companion object {
    init {
      val resolver = DefaultConfigResolver()
      val config = resolver.resolve()
      TestcontainerInitializer.initialize(config)
    }
  }

  override fun beforeAll(context: ExtensionContext) {}
}
