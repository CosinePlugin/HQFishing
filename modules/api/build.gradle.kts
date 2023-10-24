plugins {
    id("hq.shared")
    id("hq.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(framework.core)
}