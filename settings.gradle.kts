plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "rideshare-backend"

include("user-service")
project(":user-service").projectDir = file("services/user-service")
