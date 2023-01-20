package io.craigmiller160.testcontainers.common.utils

import java.io.File
import java.util.concurrent.TimeUnit

object Terminal {
  fun runCommand(command: String, init: CommandOpts.() -> Unit = {}): String {
    val opts = CommandOpts()
    opts.init()
    return ProcessBuilder("\\s".toRegex().split(command))
      .directory(opts.workingDir)
      .redirectOutput(ProcessBuilder.Redirect.PIPE)
      .redirectError(ProcessBuilder.Redirect.PIPE)
      .start()
      .also { it.waitFor(opts.timeoutAmount, opts.timeoutUnit) }
      .inputStream
      .bufferedReader()
      .readText()
  }

  class CommandOpts {
    var workingDir: File = File(".")
    var timeoutAmount: Long = 60
    var timeoutUnit: TimeUnit = TimeUnit.SECONDS
  }
}
