defaultTasks("build", "shadowJar")

allprojects {
    version = (System.getenv("VERSION") ?: "dev")

    apply(plugin = "java")

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://maven.whereareiam.me/release")
        maven("https://maven.whereareiam.me/development")
    }

    dependencies {
        // lombok
        "compileOnly"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)

        "compileOnly"(rootProject.libs.guice)
        "compileOnly"(rootProject.libs.socialismus)
    }
}