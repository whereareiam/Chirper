plugins {
    id("shared")
}

dependencies {
    compileOnly(project(":chirper-api"))
    compileOnly(libs.commandant)
    compileOnly(libs.cloud.annotations)
}
