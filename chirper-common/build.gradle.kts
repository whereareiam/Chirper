dependencies {
    "compileOnly"(project(":chirper-common-api"))
}

tasks.test {
    useJUnitPlatform()
}