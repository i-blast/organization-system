plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}
