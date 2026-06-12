plugins {
    id("runtime")
}

dependencies {
    implementation(project(":chirper-api"))
    implementation(project(":chirper-command"))
    implementation(project(":chirper-common"))
}
