plugins {
    `java-library`
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.3")
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("io.jsonwebtoken:jjwt-api:0.12.6")
    api("org.apache.commons:commons-lang3:3.18.0")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}
