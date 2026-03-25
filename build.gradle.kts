import org.gradle.api.plugins.JavaPluginExtension

plugins {
    id("org.springframework.boot") version "3.5.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    group = "com.rideshare"
    version = "0.0.1-SNAPSHOT"

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}
