package io.craigmiller160.testcontainers.common

import io.craigmiller160.testcontainers.common.config.DefaultConfigResolver
import io.craigmiller160.testcontainers.common.utils.Terminal
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestcontainersExtension : BeforeAllCallback {
  private val resolver = DefaultConfigResolver()

  override fun beforeAll(context: ExtensionContext) {
    val config = resolver.resolve()
    val containers =
      runCatching { Terminal.runCommand("docker ps") }
        .map { getContainers(it) }
        .getOrElse { listOf() }
    TODO("Not yet implemented")
  }

  private fun getContainers(output: String): List<String> =
    output
      .split("\n")
      .filter { row -> row.trim().isNotBlank() }
      .mapNotNull { row -> Regex("\\S+$").find(row)?.value }
}
