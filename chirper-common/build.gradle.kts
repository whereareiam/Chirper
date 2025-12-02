dependencies {
    "compileOnly"(project(":chirper-api"))
}

tasks.test {
    useJUnitPlatform()
}