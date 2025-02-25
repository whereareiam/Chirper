import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    "implementation"(project(":chirper-configuration"))
    "implementation"(project(":chirper-common-api"))
    "implementation"(project(":chirper-command"))
    "implementation"(project(":chirper-common"))
}

tasks.withType<ShadowJar> {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")

    relocate("com.google.inject", "me.whereareiam.socialismus.library.guice")

    destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}

tasks.named<Copy>("processResources") {
    filter<ReplaceTokens>(
        "tokens" to mapOf(
            "projectName" to rootProject.name,
            "projectVersion" to project.version
        )
    )
}

tasks.named<Jar>("jar") {
    dependsOn("shadowJar")
}