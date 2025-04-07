plugins {
    java
}

group = "com.pii.shared"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

dependencies {

    // Validation
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")

    // Misc
    compileOnly("org.projectlombok:lombok:1.18.38")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
}
