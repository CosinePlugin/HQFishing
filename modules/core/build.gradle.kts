plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.spigot)

    compileOnly(framework.core)
    compileOnly(framework.command)
    compileOnly(framework.inventory)
    compileOnly(framework.nms)

    compileOnly(project(":modules:api"))

    testImplementation(kotlin("test"))
}