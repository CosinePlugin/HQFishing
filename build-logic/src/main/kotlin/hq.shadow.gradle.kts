plugins {
    java
    id("com.github.johnrengelman.shadow")
}

tasks.shadowJar {
    archiveBaseName.set(project.rootProject.name)
    archiveVersion.set(project.rootProject.version.toString())
    archiveClassifier.set("")
    destinationDirectory.set(file("D:\\서버\\1.20.1 - 번지\\로비1\\plugins"))
}

configurations.runtimeClasspath.get().apply {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}