plugins {
    id("hq.shared")
    id("hq.shadow")
    id("kr.hqservice.resource-generator.bukkit")
}

bukkitResourceGenerator {
    val pluginName = extra["projectName"].toString()
    main = "${extra["projectGroup"]}.$pluginName"
    name = pluginName
    version = "1.0.2"
    apiVersion = "1.17"
    depend = listOf("HQFramework")
    libraries = excludedRuntimeDependencies()
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(framework.core)
    runtimeOnly(project(":modules:core"))
    runtimeOnly(project(":modules:api"))
}
