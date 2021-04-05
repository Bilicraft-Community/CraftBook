import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.internal.HasConvention

plugins {
    id("java-library")
    id("net.ltgt.apt-eclipse")
    id("net.ltgt.apt-idea")
}

applyPlatformAndCoreConfiguration()
applyShadowConfiguration()

repositories {
    maven {
        name = "paper"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "bstats"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    maven {
        name = "Vault"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "ProtocolLib"
        url = uri("http://repo.dmulloy2.net/content/groups/public/")
    }
}

dependencies {
    "compile"(project(":craftbook-core"))
    "compile"(project(":craftbook-libs:bukkit"))
    "api"("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT") {
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    "api"("com.sk89q.worldedit:worldedit-bukkit:${Versions.WORLDEDIT}") {
        exclude(group = "org.spigotmc")
    }
    "api"("com.sk89q.worldguard:worldguard-bukkit:${Versions.WORLDGUARD}") {
        exclude(group = "org.spigotmc")
    }
    "implementation"("net.milkbowl.vault:VaultAPI:1.7") { isTransitive = false }
    "implementation"("com.comphenix.protocol:ProtocolLib:4.5.1") { isTransitive = false }
    "implementation"("org.bstats:bstats-bukkit:2.2.1")

    "implementation"(platform("org.apache.logging.log4j:log4j-bom:2.8.1"))
    "implementation"("org.apache.logging.log4j:log4j-api")

    "compileOnly"("com.sk89q.worldedit.worldedit-libs:ap:${Versions.WORLDEDIT}")
    "annotationProcessor"("com.sk89q.worldedit.worldedit-libs:ap:${Versions.WORLDEDIT}")
    "annotationProcessor"("com.google.guava:guava:21.0")
}

tasks.named<Upload>("install") {
    (repositories as HasConvention).convention.getPlugin<MavenRepositoryHandlerConvention>().mavenInstaller {
        pom.whenConfigured {
            dependencies.firstOrNull { dep ->
                dep!!.withGroovyBuilder {
                    getProperty("groupId") == "com.destroystokyo.paper" && getProperty("artifactId") == "paper-api"
                }
            }?.withGroovyBuilder {
                setProperty("groupId", "org.bukkit")
                setProperty("artifactId", "bukkit")
            }
        }
    }
}

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("internalVersion" to project.ext["internalVersion"])
    }
}

addJarManifest();

tasks.named<Jar>("jar") {
    manifest {
        attributes("Implementation-Version" to project.version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    dependencies {
        relocate("org.bstats", "org.enginehub.craftbook.bukkit.bstats") {
            include(dependency("org.bstats:bstats-bukkit:2.2.1"))
            include(dependency("org.bstats:bstats-base:2.2.1"))
        }
    }
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}
