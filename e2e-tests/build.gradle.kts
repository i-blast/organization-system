plugins {
    java
    id("io.spring.dependency-management") version "1.1.7"
}
group = "com.pii.integration"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Common
    implementation(project(":common:shared-lib"))

    // Tests
    testImplementation(project(":common:shared-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.4") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:testcontainers:1.20.6")
    testImplementation("org.testcontainers:postgresql:1.20.6")
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
    testImplementation("io.rest-assured:rest-assured:5.5.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()

    dependsOn(
        ":services:company-service:bootJar",
        ":services:user-service:bootJar",
        ":services:discovery-service:bootJar"
    )
}
