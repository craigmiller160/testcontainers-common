package io.craigmiller160.testcontainers.common.utils

fun <T> T?.toResult(messageIfNull: String = ""): Result<T> =
  when (this) {
    null -> Result.failure(NullPointerException(messageIfNull))
    else -> Result.success(this)
  }
