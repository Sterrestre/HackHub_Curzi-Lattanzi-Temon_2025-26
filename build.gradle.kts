plugins {
    id("java")
}

group = "it.unicam.cs.mpgc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

// sistema di notifiche - Gmail
//    implementation 'com.google.apis:google-api-services-gmail:v1-rev110-1.25.0'
//    implementation 'com.google.oauth-client:google-oauth-client-jetty:1.34.1'
//    implementation 'com.google.http-client:google-http-client-jackson2:1.43.3'
}

tasks.test {
    useJUnitPlatform()
}