package io.craigmiller160.testcontainers.common.config

import org.yaml.snakeyaml.Yaml

class DefaultConfigResolver(private val filePath: String = "testcontainers-common.yml") :
  ConfigResolver {
  override fun resolve(): TestcontainersCommonConfig =
    javaClass.classLoader
      .getResourceAsStream(filePath)
      ?.use { stream -> Yaml().load<Map<String, Any>>(stream) }
      ?.let { TestcontainersCommonConfig(it) }
      ?: throw IllegalStateException("No $filePath file found at root of classpath")
}
