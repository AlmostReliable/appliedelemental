@file:Suppress("UnstableApiUsage")

val license: String by project
val logLevel: String by project
val recipeViewer: String by project
val mcVersion: String by project
val modVersion: String by project
val modPackage: String by project
val modId: String by project
val modName: String by project
val modAuthor: String by project
val modDescription: String by project
val neoVersion: String by project
val parchmentVersion: String by project
val aeVersion: String by project
val elementalVersion: String by project
val dpAnvilVersion: String by project
val jeiVersion: String by project
val reiVersion: String by project
val emiVersion: String by project
val githubUser: String by project
val githubRepo: String by project

plugins {
    id("net.neoforged.gradle.userdev") version "7.0.107"
    id("com.github.gmazzo.buildconfig") version "5.3.5"
    java
}

base {
    version = "$mcVersion-$modVersion"
    group = modPackage
    archivesName.set("$modId-neoforge")
}

sourceSets {
    main {
        resources {
            srcDir(file("src/main/generated"))
            exclude("**/.cache")
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

val commonSystemProperties = mapOf(
    "forge.logging.console.level" to logLevel,
    "guideDev.ae2guide.sources" to file("guidebook").absolutePath,
    "guideDev.ae2guide.sourcesNamespace" to modId
)

runs {
    configureEach {
        workingDirectory = project.file("run")
        systemProperties = commonSystemProperties
        modSource(sourceSets.main.get())
        jvmArguments("-XX:+IgnoreUnrecognizedVMOptions", "-XX:+AllowEnhancedClassRedefinition")
    }

    create("client") {
        programArguments("--quickPlaySingleplayer", "New World")
    }
    create("data") {
        programArguments("--all", "--mod", modId)
        programArguments("--output", file("src/main/generated").absolutePath)
        programArguments("--existing", file("src/main/resources").absolutePath)
        programArguments("--existing-mod", "ae2")
        programArguments("--existing-mod", "elementalcraft")
    }
    create("guide") {
        configure("client")
        systemProperty("guideDev.ae2guide.startupPage", "$modId:$modId.md")
    }
    create("server")
}

repositories {
    mavenWithGroup("https://maven.neoforged.net/releases", "net.neoforged") // NeoForge
    mavenWithGroup("https://modmaven.dev", "appeng") // Applied Energistics 2
    mavenWithGroup("https://repo.elementalcraft.org/releases", "sirttas.elementalcraft", "sirttas.dpanvil") // ElementalCraft
    mavenWithGroup("https://maven.blamejared.com", "mezz.jei") // JEI
    mavenWithGroup("https://maven.shedaniel.me", "me.shedaniel") // REI
    mavenWithGroup("https://maven.terraformersmc.com", "dev.emi") // EMI
    mavenLocal()
}

dependencies {
    // NeoForge
    implementation("net.neoforged:neoforge:$neoVersion")

    // Applied Energistics 2
    implementation("appeng:appliedenergistics2-neoforge:$aeVersion")
    // ElementalCraft
    implementation("sirttas.elementalcraft:ElementalCraft:$elementalVersion")
    // DPAnvil (ElementalCraft dependency)
    runtimeOnly("sirttas.dpanvil:DPAnvil:$dpAnvilVersion")

    // Recipe Viewers
    when (recipeViewer) {
        "jei" -> {
            compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:$jeiVersion") { isTransitive = false }
            runtimeOnly("mezz.jei:jei-$mcVersion-neoforge:$jeiVersion") { isTransitive = false }
            compileOnly("dev.emi:emi-neoforge:$emiVersion+$mcVersion")
        }

        "rei" -> {
            runtimeOnly("me.shedaniel:RoughlyEnoughItems-neoforge:$reiVersion")
            compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:$jeiVersion") { isTransitive = false }
            compileOnly("dev.emi:emi-neoforge:$emiVersion+$mcVersion")
        }

        "emi" -> {
            implementation("dev.emi:emi-neoforge:$emiVersion+$mcVersion")
            implementation("mezz.jei:jei-$mcVersion-neoforge:$jeiVersion") { isTransitive = false }
        }

        else -> throw GradleException("Invalid recipeViewer value: $recipeViewer")
    }
}

tasks {
    processResources {
        val resourceTargets = listOf("META-INF/mods.toml", "pack.mcmeta")

        val replaceProperties = mapOf(
            "license" to license,
            "mcVersion" to mcVersion,
            "version" to project.version as String,
            "modId" to modId,
            "modName" to modName,
            "modAuthor" to modAuthor,
            "modDescription" to modDescription,
            "neoVersion" to neoVersion,
            "aeVersion" to aeVersion,
            "elementalVersion" to elementalVersion,
            "githubUser" to githubUser,
            "githubRepo" to githubRepo
        )

        println("[Process Resources] Replacing properties in resources: ")
        replaceProperties.forEach { (key, value) -> println("\t -> $key = $value") }

        inputs.properties(replaceProperties)
        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<Jar> {
        from("guidebook") {
            into("assets/$modId/ae2guide")
        }
    }

    withType<GenerateModuleMetadata> {
        enabled = false
    }
}

subsystems.parchment {
    minecraftVersion(mcVersion)
    mappingsVersion(parchmentVersion)
}

buildConfig {
    buildConfigField("String", "MOD_ID", "\"$modId\"")
    buildConfigField("String", "MOD_NAME", "\"$modName\"")
    buildConfigField("String", "MOD_VERSION", "\"$version\"")
    packageName(modPackage)
    useJavaOutput()
}

fun RepositoryHandler.mavenWithGroup(url: String, vararg group: String) {
    maven(url) {
        content {
            group.forEach { includeGroup(it) }
        }
    }
}
