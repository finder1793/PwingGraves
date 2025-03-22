plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.pwing.graves"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.elmakers.com/repository/")
    maven("https://jitpack.io")
    maven("https://repo.helpch.at/releases")
    maven("https://repo.skriptlang.org/releases")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:Vault:1.7.3")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("com.github.SkriptLang:Skript:2.6.4")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
    compileOnly("com.nexomc:nexo:1.2.0") {
        exclude("*", "*")
    }
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:32.1.3-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        expand(project.properties)
    }
}
