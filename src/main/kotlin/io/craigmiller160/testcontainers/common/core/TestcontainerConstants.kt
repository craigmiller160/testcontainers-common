package io.craigmiller160.testcontainers.common.core

object TestcontainerConstants {
  const val KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:20.0.2"
  const val KEYCLOAK_REALM_FILE = "keycloak-realm.json"
  const val KEYCLOAK_ADMIN_USER = "admin"
  const val KEYCLOAK_ADMIN_PASSWORD = "password"
  const val KEYCLOAK_URL_PROP = "keycloak.url"

  const val POSTGRES_IMAGE = "postgres:14.5"
  const val POSTGRES_USER = "user"
  const val POSTGRES_PASSWORD = "password"
  const val POSTGRES_DB_NAME = "test"
  const val POSTGRES_URL_PROP = "postgres.url"
  const val POSTGRES_USER_PROP = "postgres.username"
  const val POSTGRES_PASSWORD_PROP = "postgres.password"
}
