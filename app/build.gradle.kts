plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("org.sonarqube") version "7.2.2.6593"
    id("jacoco")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.check {
    dependsOn(tasks.checkstyleMain, tasks.checkstyleTest)
}

tasks.build {
    dependsOn(tasks.check)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // Для генерации отчета о покрытии
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true) // SonarQube требует XML отчет
        html.required.set(true)
    }
}

application {
    mainClass = "hexlet.code.App"
}

sonar {
    properties {
        property("sonar.projectKey", "valentin-osadchii_java-project-72")
        property("sonar.organization", "valentin-osadchii")
    }
}

