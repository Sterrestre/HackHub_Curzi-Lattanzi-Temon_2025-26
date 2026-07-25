plugins {
    id("java")
    id ("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "it.unicam.cs"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot base
    implementation ("org.springframework.boot:spring-boot-starter-web")

    // Database MySQL
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly ("com.mysql:mysql-connector-j")

    // H2 solo per i test unitari (DB in-memory, non usato in produzione/docker)
    testRuntimeOnly ("com.h2database:h2")

    // Validazioni (opzionale ma utile)
    implementation ("org.springframework.boot:spring-boot-starter-validation")

//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation ("org.springframework.boot:spring-boot-starter-test")

// sistema di notifiche - Gmail
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.apis:google-api-services-gmail:v1-rev110-1.25.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.3")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
}

tasks.test {
    useJUnitPlatform()
}