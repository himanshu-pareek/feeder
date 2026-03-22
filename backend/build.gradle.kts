plugins {
    application
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.rometools:rome:2.1.0")
    implementation(libs.guava)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "dev.javarush.feeder.FeederWebApplication"
}

tasks.register<JavaExec>("cli") {
    group = "application"
    description = "Runs the Feeder CLI application with interactive input."
    mainClass = "dev.javarush.feeder.cli.FeederApp"
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
