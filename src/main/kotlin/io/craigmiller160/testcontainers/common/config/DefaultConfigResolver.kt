package io.craigmiller160.testcontainers.common.config

import org.yaml.snakeyaml.Yaml

class DefaultConfigResolver : ConfigResolver {
  override fun resolve(): TestcontainersCommonConfig =
    javaClass.classLoader
      .getResourceAsStream("testcontainers-common.yml")
      ?.use { stream -> Yaml().load<Map<String, Any>>(stream) }
      ?.let { TestcontainersCommonConfig(it) }
      ?: throw IllegalStateException("No testcontainers-common.yml file found at root of classpath")
}
