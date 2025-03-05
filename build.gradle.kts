@file:Suppress("PropertyName")

import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.compiler
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    }
}

plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version libs.versions.kotlinVersion
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("eclipse")
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.27"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

val mod_version: String by project
val maven_group: String by project

version = mod_version
group = maven_group

val archives_base_name: String by project

val use_access_transformer: String by project

val use_mixins: String by project
val use_coremod: String by project
val use_assetmover: String by project

val include_mod: String by project
val coremod_plugin_class_name: String by project

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
        vendor.set(JvmVendorSpec.AZUL)
    }
    // Generate sources and javadocs jars when building and publishing
    withSourcesJar()
    // withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

configurations {
    val embed = create("embed")
    implementation.configure {
        extendsFrom(embed)
    }
}

minecraft {
    mcVersion.set("1.12.2")

    // MCP Mappings
    mcpMappingChannel.set("stable")
    mcpMappingVersion.set("39")

    // Set username here, the UUID will be looked up automatically
    username.set("Developer")

    // Add any additional tweaker classes here
    // extraTweakClasses.add("org.spongepowered.asm.launch.MixinTweaker")

    // Add various JVM arguments here for runtime
    val args = mutableListOf("-ea:${group}")
    if (use_coremod.toBoolean()) {
        args += "-Dfml.coreMods.load=$coremod_plugin_class_name"
    }
    if (use_mixins.toBoolean()) {
        args += "-Dmixin.hotSwap=true"
        args += "-Dmixin.checks.interfaces=true"
        args += "-Dmixin.debug.export=true"
    }
    extraRunJvmArguments.addAll(args)

    // Include and use dependencies' Access Transformer files
    useDependencyAccessTransformers.set(true)

    // Add any properties you want to swap out for a dynamic value at build time here
    // Any properties here will be added to a class at build time, the name can be configured below
    injectedTags.put("VERSION", mod_version)
}

// Generate a group.archives_base_name.Tags class
tasks.injectTags.configure {
    // Change Tags class' name here:
    outputClassName.set("${maven_group}.${archives_base_name.lowercase()}.Tags")
}

repositories {
    maven {
        name = "CleanroomMC Maven"
        url = uri("https://maven.cleanroommc.com")
    }
    maven {
        name = "SpongePowered Maven"
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
    mavenLocal() // Must be last for caching to work
}

dependencies {
    implementation("io.github.chaosunity.forgelin:Forgelin-Continuous:1.9.23.0") {
        exclude("net.minecraftforge")
    }

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlinVersion.get()}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    // Fluidlogged API
    implementation(rfg.deobf("com.github.jbredwards:fluidlogged-api:1.12.2-Latest-SNAPSHOT"))

    // Biomes O' Plenty
    implementation(rfg.deobf("curse.maven:bop-220318:2842510"))

    // Serene Seasons
    implementation(rfg.deobf("curse.maven:sereneseasons-291874:2799213"))

    // Streams
    runtimeOnly(rfg.deobf("curse.maven:streams-229769:3109315"))
    runtimeOnly(rfg.deobf("curse.maven:farseek-229708:3093852"))

    // Dynamic Trees
    compileOnly("curse.maven:dynamictrees-252818:3812474")

    // Simple Difficulty
//    runtimeOnly("curse.maven:simpledifficulty-for-underdog-574162:4434298")

    if (use_assetmover.toBoolean()) {
        implementation("com.cleanroommc:assetmover:2.5")
    }
    if (use_mixins.toBoolean()) {
        implementation("zone.rong:mixinbooter:7.1")
    }

    if (use_mixins.toBoolean()) {
        // Change your mixin refmap name here:
        val mixin =
            modUtils.enableMixins("org.spongepowered:mixin:0.8.3", "mixins.${archives_base_name.lowercase()}.refmap.json") as String
        api(mixin) {
            isTransitive = true
        }
        annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
        annotationProcessor("com.google.guava:guava:24.1.1-jre")
        annotationProcessor("com.google.code.gson:gson:2.8.6")
        annotationProcessor(mixin) {
            isTransitive = false
        }
    }
}

// Adds Access Transformer files to tasks
if (use_access_transformer.toBoolean()) {
    for (at in sourceSets.getByName("main").resources.files) {
        if (at.name.lowercase().endsWith("_at.cfg")) {
            tasks.deobfuscateMergedJarToSrg.get().accessTransformerFiles.from(at)
            tasks.srgifyBinpatchedJar.get().accessTransformerFiles.from(at)
        }
    }
}

@Suppress("UnstableApiUsage")
tasks.withType<ProcessResources> {
    // This will ensure that this task is redone when the versions change
    inputs.property("version", mod_version)
    inputs.property("mcversion", minecraft.mcVersion)

    // Replace various properties in mcmod.info and pack.mcmeta if applicable
    filesMatching(arrayListOf("mcmod.info", "pack.mcmeta")) {
        expand(
            "version" to mod_version,
            "mcversion" to minecraft.mcVersion
        )
    }

    if (use_access_transformer.toBoolean()) {
        rename("(.+_at.cfg)", "META-INF/$1") // Make sure Access Transformer files are in META-INF folder
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("$archives_base_name-${minecraft.mcVersion.get()}")

    manifest {
        val attributeMap = mutableMapOf<String, String>()
        if (use_coremod.toBoolean()) {
            attributeMap["FMLCorePlugin"] = coremod_plugin_class_name
            if (include_mod.toBoolean()) {
                attributeMap["FMLCorePluginContainsFMLMod"] = true.toString()
                attributeMap["ForceLoadAsMod"] = (project.gradle.startParameter.taskNames[0] == "build").toString()
            }
        }
        if (use_access_transformer.toBoolean()) {
            attributeMap["FMLAT"] = archives_base_name.lowercase() + "_at.cfg"
        }
        attributes(attributeMap)
    }
    // Add all embedded dependencies into the jar
    from(provider {
        configurations.getByName("embed").map {
            if (it.isDirectory()) it else zipTree(it)
        }
    })
}

idea {
    module {
        inheritOutputDirs = true
    }
    project {
        settings {
            runConfigurations {
                add(Gradle("1. Run Client").apply {
                    setProperty("taskNames", listOf("runClient"))
                })
                add(Gradle("2. Run Server").apply {
                    setProperty("taskNames", listOf("runServer"))
                })
                add(Gradle("3. Run Obfuscated Client").apply {
                    setProperty("taskNames", listOf("runObfClient"))
                })
                add(Gradle("4. Run Obfuscated Server").apply {
                    setProperty("taskNames", listOf("runObfServer"))
                })
            }
            compiler.javac {
                afterEvaluate {
                    javacAdditionalOptions = "-encoding utf8"
                    moduleJavacAdditionalOptions = mutableMapOf(
                        (project.name + ".main") to tasks.compileJava.get().options.compilerArgs.joinToString(" ") { "\"$it\"" }
                    )
                }
            }
        }
    }
}

tasks.named("processIdeaSettings").configure {
    dependsOn("injectTags")
}