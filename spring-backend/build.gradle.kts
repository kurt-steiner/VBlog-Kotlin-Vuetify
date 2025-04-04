plugins {
    `java-library`
    java
    alias(libs.plugins.springboot)
    alias(libs.plugins.springboot.dependency.management)
}

repositories {
    mavenCentral()
}

springBoot {
    mainClass = "com.steiner.backend.SpringBackendApplication"
}

dependencies {
    implementation(libs.springboot.starter.mybatis)
    implementation(libs.springboot.starter.security)
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.validation)
    implementation(libs.springboot.starter.test)
    implementation(libs.springboot.starter.mybatis.test)
    implementation(libs.postgresql)
    implementation(libs.javajwt)
    implementation(libs.lombok)
    implementation(libs.jsoup)
    annotationProcessor(libs.lombok)
}

tasks.withType<Test> {
    useJUnitPlatform()
}