package us.craigmiller160.testcontainers.common.config

interface ConfigResolver {
  fun resolve(): TestcontainersCommonConfig
}
