package io.craigmiller160.testcontainers.common.utils

import java.io.File
import java.util.concurrent.TimeUnit

object Terminal {
  fun execute(command: String): String {
    val processBuilder =
        ProcessBuilder("\\s".toRegex().split(command))
            .directory(File("."))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
    return runCatching { processBuilder.start() }
        .mapCatching { process -> process.also { process.waitFor(60, TimeUnit.SECONDS) } }
        .mapCatching { process -> process.inputStream.bufferedReader().readText() }
        .getOrThrow()
  }
}
