plugins{
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "de.medieninformatik.client"
version = "1.0-SNAPSHOT"

val javaVersion = JavaVersion.VERSION_17

application{
    mainClass.set("${group}.Main")
}

javafx{
    version = javaVersion.toString()
    modules("javafx.controls", "javafx.graphics")
}

dependencies{
    implementation("org.glassfish.jersey.core:jersey-client:3.1.4")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.4")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")

    implementation(project(":Common"))
}