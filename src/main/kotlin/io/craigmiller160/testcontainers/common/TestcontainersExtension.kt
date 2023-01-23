package io.craigmiller160.testcontainers.common

import io.craigmiller160.testcontainers.common.config.DefaultConfigResolver
import io.craigmiller160.testcontainers.common.core.TestcontainerInitializer
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestcontainersExtension : BeforeAllCallback {
  companion object {
    init {
      val resolver = DefaultConfigResolver()
      val config = resolver.resolve()
      TestcontainerInitializer.initialize(config)
      // TODO add logging
    }
  }

  override fun beforeAll(context: ExtensionContext) {}
}
