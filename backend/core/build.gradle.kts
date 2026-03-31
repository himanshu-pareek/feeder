plugins {
    id("java")
}

group = "dev.javarush.feeder"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rometools:rome:2.1.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}