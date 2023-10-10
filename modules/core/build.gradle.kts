plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.spigot.api)

    compileOnly(framework.core)
    compileOnly(framework.command)
    compileOnly(framework.inventory)
    compileOnly(framework.nms)

    compileOnly(project(":modules:api"))
}