plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.paper.api)

    compileOnly(framework.core) {
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(framework.command) {
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(framework.inventory) {
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(framework.nms) {
        exclude("org.spigotmc", "spigot-api")
    }

    compileOnly(project(":modules:api"))

    testImplementation(kotlin("test"))
}