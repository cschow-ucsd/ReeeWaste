plugins {
    id("org.jetbrains.kotlin.js")
}


repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
}
kotlin {
    sourceSets["main"].dependencies {
        implementation("org.jetbrains:kotlin-react:16.9.0-pre.89-kotlin-1.3.60")
        implementation("org.jetbrains:kotlin-react-dom:16.9.0-pre.89-kotlin-1.3.60")
        implementation(npm("react", "16.12.0"))
        implementation(npm("react-dom", "16.12.0"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3")

        implementation(project(":common"))
    }
    sourceSets["test"].dependencies {
        implementation(kotlin("test-js"))
    }
}

kotlin.target.browser { }