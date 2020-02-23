buildscript {
    extra.apply {
        set("kotlin_version", "1.3.61")
        set("ktor_version", "1.3.1")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
    }

    dependencies {
        val kotlin_version: String by extra

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:3.5.1")
    }
}

group = "project.ucsd.reee_waste"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kotlin/ktor")
        maven(url = "https://dl.bintray.com/kodein-framework/Kodein-DI")
        maven(url = "https://dl.bintray.com/touchlabpublic/kotlin")
    }
}