import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.gradle.kotlin.dsl.build
import org.gradle.kotlin.dsl.invoke
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

plugins {
    kotlin("jvm") version "2.2.20-Beta1"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

buildscript {
    dependencies {
        classpath("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    }
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
//    runServer {
//        runDirectory = file("run/server/main")
//        dependsOn("build")
//        // Configure the Minecraft version for our task.
//        // This is the only required configuration besides applying the plugin.
//        // Your plugin's jar (or shadowJar if present) will be used automatically.
//        minecraftVersion("1.21.6")
//    }
}

tasks.register("setupServer") {
    group = "server"

    dependsOn("downloadPaper")
    dependsOn("downloadVelocity")

    doLast {

        println("Before you start the paper and velocity servers, you need to follow a few steps\n\n" +
                "1. Run both servers `runPaperServer` and `runVelocityServer` (these might error, but this generates all the required files)\n" +
                "2. Go to `run/server/proxy` and in the `velocity.toml` change `player-info-forwarding-mode` to `modern` for security purposes\n" +
                "3. Go to `run/server/main` and in the `server.properties` change `online-mode` to false. Then, change any instance of `25565` to an open port\n" +
                "4. Go to `run/server/proxy` and, under the servers section, delete all the defaults and add `main = 127.0.0.1:` followed by the port you inserted before. Then, change the `try` array to just `main`" +
                "5. Go to `run/server/main/config` and in the `paper-global.yml` change the `velocity.enabled` field to `true`\n" +
                "6. Go to `run/server/proxy` and copy the value from `forwarding.secret` into the `velocity.secret` field in the `paper-global.yml`\n" +
                "7. Do the config shenanigans in `run/server/main/plugins/TreeTumblers\n\n" +
                "Happy tumbling!")
    }
}

tasks.register("downloadPaper") {
    group = "server"
    doLast {
        val targetDir = file("run/server/main")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val apiUrl = "https://fill.papermc.io/v3/projects/paper/versions/1.21.6/builds/latest"
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val mapper = jacksonObjectMapper()
        val json: Map<String, Any> = mapper.readValue(response.body())
        val downloads = (json["downloads"] as Map<*, *>)
        val serverDefault = downloads["server:default"] as Map<*, *>
        val downloadUrl = serverDefault["url"] as String

        println("Downloading Paper server from: $downloadUrl")
        val fileRequest = HttpRequest.newBuilder()
            .uri(URI.create(downloadUrl))
            .build()
        val fileResponse = client.send(fileRequest, HttpResponse.BodyHandlers.ofByteArray())
        File(targetDir, "paper.jar").writeBytes(fileResponse.body())
        File(targetDir, "eula.txt").writeText("eula=true")
        println("Downloaded to run/server/main/paper.jar")
    }
}

tasks.register("downloadVelocity") {
    group = "server"
    doLast {
        val targetDir = file("run/server/proxy")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val apiUrl = "https://fill.papermc.io/v3/projects/velocity/versions/3.4.0-SNAPSHOT/builds/latest"
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val mapper = jacksonObjectMapper()
        val json: Map<String, Any> = mapper.readValue(response.body())
        val downloads = (json["downloads"] as Map<*, *>)
        val serverDefault = downloads["server:default"] as Map<*, *>
        val downloadUrl = serverDefault["url"] as String

        println("Downloading Velocity proxy from: $downloadUrl")
        val fileRequest = HttpRequest.newBuilder()
            .uri(URI.create(downloadUrl))
            .build()
        val fileResponse = client.send(fileRequest, HttpResponse.BodyHandlers.ofByteArray())
        File(targetDir, "velocity.jar").writeBytes(fileResponse.body())
        println("Downloaded to run/server/proxy/velocity.jar")
    }
}

tasks.register<Exec>("runPaperServer") {
    group = "server"
    description = "Runs the Paper server"
    workingDir = file("run/server/main")
    commandLine = listOf(
        "java",
        "-Xmx8G",
        "-jar",
        "paper.jar",
        "nogui"
    )
    standardInput = System.`in`

    dependsOn("build")
    doFirst {
        val jarFile = file("run/server/main/paper.jar")
        if(!jarFile.exists()) {
            throw GradleException("Paper not installed. You need to run `gradlew setupServer` first.")
        }

        val pluginsDir = file("run/server/main/plugins")
        if(!pluginsDir.exists()) {
            pluginsDir.mkdirs()
        }

        val jar = file("build/libs/${project.name}-${project.version}-all.jar")
        if (jar.exists()) {
            jar.copyTo(File(pluginsDir, jar.name), overwrite = true)
        } else {
            throw GradleException("Plugin JAR not found: ${jar.path}")
        }
    }
}

tasks.register<Exec>("runVelocityServer") {
    group = "server"
    description = "Runs the Velocity proxy"
    workingDir = file("run/server/proxy")
    commandLine = listOf(
        "java",
        "-Xmx2G",
        "-jar",
        "velocity.jar",
        "nogui"
    )
    standardInput = System.`in`
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
