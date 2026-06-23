plugins {
    `java-library`
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val buildVersion = providers.environmentVariable("VERSION").orElse("dev")

group = "me.whereareiam"
version = buildVersion.get()

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}

dependencies {
    add("compileOnly", libs.findLibrary("lombok").get())
    add("annotationProcessor", libs.findLibrary("lombok").get())
    add("testCompileOnly", libs.findLibrary("lombok").get())
    add("testAnnotationProcessor", libs.findLibrary("lombok").get())

    add("compileOnly", libs.findLibrary("annotations").get())
    add("testCompileOnly", libs.findLibrary("annotations").get())
    add("compileOnly", libs.findLibrary("guice").get())
    add("testImplementation", libs.findLibrary("guice").get())
    add("testImplementation", libs.findLibrary("adventure-api").get())
    add("compileOnly", libs.findLibrary("socialismus-module-api").get())
    add("testImplementation", libs.findLibrary("socialismus-module-api").get())

    add("testImplementation", libs.findBundle("testing").get())
    add("testRuntimeOnly", libs.findLibrary("junit-platform").get())

    if (path != ":chirper-api") {
        add("compileOnly", project(":chirper-api"))
        add("testImplementation", project(":chirper-api"))
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
