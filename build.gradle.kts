plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.0"
}

group = "dev.digitality"
version = "2.0.0"
description = "VelocityCommands"

java.toolchain {
    languageVersion = JavaLanguageVersion.of(25)
    vendor = JvmVendorSpec.ADOPTIUM
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    implementation("org.yaml:snakeyaml:2.6")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}

tasks {

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("org.yaml.snakeyaml", "dev.digitality.velocitycommands.libs.snakeyaml")

        archiveFileName.set("${project.name}.jar")
    }

    processResources {
        filteringCharset = "UTF-8"
    }
}
