import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
}

apply(plugin = "io.github.goooler.shadow")

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
}

tasks.named<Jar>("jar") {
    dependsOn("shadowJar")
}