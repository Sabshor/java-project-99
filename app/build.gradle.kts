plugins {
    application
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.freefair.lombok") version "8.6"
    checkstyle
    jacoco
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.AppApplication")
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("com.h2database:h2")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("net.datafaker:datafaker:2.0.2")
    implementation("org.instancio:instancio-junit:3.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}