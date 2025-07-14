import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

plugins {
    kotlin("jvm") version "2.2.20-Beta1"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "xyz.devcmb"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://maven.noxcrew.com/public") {
        name = "noxcrew"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
    implementation("com.noxcrew.interfaces:interfaces:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks {
    runServer {
        dependsOn("build")
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.6")
    }
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
    dependsOn("updateVersion")
}

fun padLeftZeros(inputString: String, length: Int): String {
    if (inputString.length >= length) return inputString
    val sb = StringBuilder()
    while (sb.length < length - inputString.length) {
        sb.append('0')
    }
    sb.append(inputString)
    return sb.toString()
}

tasks.register("updateVersion") {
    doLast {
        val versionFile = file("src/main/kotlin/xyz/devcmb/treeTumblers/Constants.kt" +
                "")
        val versionCounterFile = file("versionCounter.txt")
        val newVersion = project.version.toString()

        var counter = 0
        if (versionCounterFile.exists()) {
            counter = versionCounterFile.readText().trim().toInt(16)
        }

        counter++
        val hexCounter = counter.toString(16)
        val updatedVersion = "$newVersion-${padLeftZeros(hexCounter, 6)}"
        val content = versionFile.readText()
        val updatedContent = content.replace(
            Regex("""(const val VERSION: String = ")([^"]+)(")"""),
            "$1$updatedVersion$3"
        )
        Files.write(
            Paths.get(versionFile.toURI()),
            updatedContent.toByteArray(),
            StandardOpenOption.TRUNCATE_EXISTING
        )
        versionCounterFile.writeText(hexCounter)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
