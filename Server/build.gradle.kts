plugins{
    application
}

group = "de.medieninformatik.server"
version = "1.0-SNAPSHOT"

application{
    mainClass.set("${group}.Main")
}

dependencies{
    implementation("org.glassfish.jersey.core:jersey-server:3.1.4")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.4")
    implementation("org.glassfish.grizzly:grizzly-http-server:4.0.1")
    implementation("org.glassfish.grizzly:grizzly-websockets:4.0.1")
    implementation("org.glassfish.jersey.containers:jersey-container-grizzly2-http:3.1.4")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4")
    implementation("org.glassfish:javax.json:1.1.4")

    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.1")
    implementation("org.glassfish.grizzly:grizzly-framework:4.0.1")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")

    implementation(project(":Common"))
}

tasks.named<JavaExec>("run"){
    setStandardInput(System.`in`)
    setStandardOutput(System.out)
}

tasks.withType<Jar>{
    manifest{
        attributes("Main-Class" to application.mainClass.get())
    }
}