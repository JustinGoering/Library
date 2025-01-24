plugins {
    java
}

group = "de.medieninformatik"
version = "1.0-SNAPSHOT"

val javaVersion = JavaVersion.VERSION_17

subprojects{

    apply(plugin ="java")

    repositories {
        mavenCentral()
    }

    java{
        setSourceCompatibility(javaVersion)
        setTargetCompatibility(javaVersion)
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}