plugins {
    java
    id("site.siredvin.root") version "0.4.12"
    id("site.siredvin.release") version "0.4.12"
}

subprojectShaking {
    withKotlin.set(true)
    kotlinVersion.set("1.9.0")
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    setupSubproject(this)
}

githubShaking {
    modBranch.set("1.20")
    shake()
}

repositories {
    mavenCentral()
}