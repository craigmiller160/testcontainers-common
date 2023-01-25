package io.craigmiller160.testcontainers.common.core

object TestcontainerConstants {
  const val KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:20.0.2"
  const val KEYCLOAK_REALM_FILE = "keycloak-realm.json"
  const val KEYCLOAK_ADMIN_USER = "admin"
  const val KEYCLOAK_ADMIN_PASSWORD = "password"
  const val KEYCLOAK_URL_PROP = "testcontainers.common.keycloak.url"
  const val KEYCLOAK_REALM_PROP = "testcontainers.common.keycloak.realm"
  const val KEYCLOAK_CLIENT_ID_PROP = "testcontainers.common.keycloak.client.id"
  const val KEYCLOAK_CLIENT_SECRET_PROP = "testcontainers.common.keycloak.client.secret"
  const val KEYCLOAK_REALM = "apps-dev"
  const val KEYCLOAK_CLIENT_ID = "test-client"
  const val KEYCLOAK_CLIENT_SECRET = "CQ60kzXcV7F31JfW8dX2MINjVIKOIxpJ"
  const val KEYCLOAK_ADMIN_USER_PROP = "testcontainers.common.keycloak.admin.user"
  const val KEYCLOAK_ADMIN_PASSWORD_PROP = "testcontainers.common.keycloak.admin.password"

  const val POSTGRES_IMAGE = "postgres:14.5"
  const val POSTGRES_USER = "user"
  const val POSTGRES_PASSWORD = "password"
  const val POSTGRES_DB_NAME = "test"
  const val POSTGRES_URL_PROP = "testcontainers.common.postgres.url"
  const val POSTGRES_USER_PROP = "testcontainers.common.postgres.user"
  const val POSTGRES_PASSWORD_PROP = "testcontainers.common.postgres.password"
  const val POSTGRES_SCHEMA_PROP = "testcontainers.common.postgres.schema"
}
