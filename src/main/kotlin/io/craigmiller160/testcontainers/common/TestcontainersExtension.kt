package io.craigmiller160.testcontainers.common

import io.craigmiller160.testcontainers.common.config.DefaultConfigResolver
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestcontainersExtension : BeforeAllCallback {
  private val resolver = DefaultConfigResolver()

  override fun beforeAll(context: ExtensionContext) {
    val config = resolver.resolve()
    TODO("Not yet implemented")
  }
}
