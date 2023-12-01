plugins {
    application
    alias(dolbyio.plugins.kotlin.jvm)
    alias(libs.plugins.shadowJar)
    id("jvmCompat")
}

val mainClassInManifest = "com.lorcanaapi.App"

group = "com.lorcanaapi"
version = "0.0.1"

repositories {
    mavenCentral()
    google()
}

application {
    mainClass.set(mainClassInManifest)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = mainClassInManifest
    }
}

dependencies {
    testApi(kotlin("test"))
    testApi(dolbyio.junit.jupiter.api)

    api(libs.json)
    api(libs.fuzzywuzzy)
    api(libs.mysql.connector.j)
}
