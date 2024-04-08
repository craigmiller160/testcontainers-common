package io.craigmiller160.testcontainers.common.config

import io.craigmiller160.testcontainers.common.utils.toResult
import org.yaml.snakeyaml.Yaml

class DefaultConfigResolver(private val filePath: String = "testcontainers-common.yml") :
    ConfigResolver {
  override fun resolve(): TestcontainersCommonConfig =
      javaClass.classLoader
          .getResourceAsStream(filePath)
          .toResult("No $filePath file found at root of classpath")
          .mapCatching { Yaml().load<Map<String, Any>>(it) }
          .mapCatching { yaml -> TestcontainersCommonConfig(yaml ?: mapOf()) }
          .getOrThrow()
}
