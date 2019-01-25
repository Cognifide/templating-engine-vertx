dependencies {
  "implementation"(platform("io.knotx:knotx-dependencies:${project.version}"))
  "implementation"(group = "io.vertx", name = "vertx-core")
  "implementation"(group = "io.vertx", name = "vertx-service-proxy")
  "implementation"(group = "io.vertx", name = "vertx-rx-java2")
  "implementation"(group = "io.vertx", name = "vertx-codegen")
  "implementation"(group = "io.vertx", name = "vertx-config")
  "implementation"(group = "io.vertx", name = "vertx-config-hocon")
  "implementation"(group = "io.vertx", name = "vertx-web")
  "implementation"(group = "io.vertx", name = "vertx-web-api-contract")
  "implementation"(group = "io.vertx", name = "vertx-web-client")
  "implementation"(group = "io.vertx", name = "vertx-service-discovery")
  "implementation"(group = "io.vertx", name = "vertx-circuit-breaker")
  "implementation"(group = "io.vertx", name = "vertx-hazelcast")

  "testImplementation"(group = "io.knotx", name = "knotx-junit5")
  "testImplementation"(group = "io.vertx", name = "vertx-junit5")
  "testImplementation"(group = "org.junit.jupiter", name = "junit-jupiter-api")
  "testImplementation"(group = "org.junit.jupiter", name = "junit-jupiter-params")
  "testImplementation"(group = "org.junit.jupiter", name = "junit-jupiter-migrationsupport")
  "testImplementation"(group = "io.vertx", name = "vertx-unit")
  "testImplementation"(group = "com.github.stefanbirkner", name = "system-rules") {
    exclude(module = "junit-dep")
  }
  "testImplementation"(group = "com.googlecode.zohhak", name = "zohhak")
  "testImplementation"(group = "uk.co.datumedge", name = "hamcrest-json")
  "testImplementation"(group = "org.hamcrest", name = "hamcrest-all")

  "testImplementation"(group = "io.vertx", name = "vertx-core")
  "testImplementation"(group = "io.vertx", name = "vertx-web")
  "testImplementation"(group = "io.vertx", name = "vertx-web-api-contract")
  "testImplementation"(group = "io.vertx", name = "vertx-web-client")
  "testImplementation"(group = "io.vertx", name = "vertx-rx-java2")
  "testImplementation"(group = "io.vertx", name = "vertx-service-proxy")
  "testImplementation"(group = "io.vertx", name = "vertx-config")
  "testImplementation"(group = "io.vertx", name = "vertx-config-hocon")
  "testImplementation"(group = "io.vertx", name = "vertx-hazelcast")
}
