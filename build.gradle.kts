buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Build.kotlinGradlePlugin)
        classpath(Build.buildTools)
        classpath(Build.navSafeArgs)
        classpath(Build.hiltGradlePlugin)
        classpath(Build.sqlDelightGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}