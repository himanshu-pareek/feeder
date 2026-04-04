import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    application
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.javarush.feeder"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(project(":core"))
    implementation(project(":memory"))
    implementation(project(":postgresql"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "dev.javarush.feeder.api.FeederAPI"
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<BootRun>("bootRun") {
    readDotEnvFile(file(".env"))
}

fun BootRun.readDotEnvFile(envFile: File) {
    println("Trying to read environment variables from $envFile...")
    if (envFile.exists()) {
        println("Environment variables from file $envFile will be loaded since it exists.")
        envFile.readLines()
            .filter { !it.isBlank() }
            .filter { !it.startsWith("#") }
            .filter { it.contains("=") }
            .forEach {
                val (key, value) = it.split("=", limit = 2)
                environment(key, value)
            }
    } else {
        println("$envFile does not exist. Skipping...")
    }
}
