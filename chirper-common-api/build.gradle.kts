java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "Chirper"
            pom {
                name.set("Chirper")
                description.set("Public API for Chirper - Socialismus announcement module")
            }
        }
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
        title = "Chirper API"
        windowTitle = "Chirper API"
    }
}