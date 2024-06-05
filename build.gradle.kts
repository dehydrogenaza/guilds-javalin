val javalinVersion = "6.1.3"

plugins {
    id("java")
}

group = "sm.guilds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("io.javalin:javalin:${javalinVersion}") //bare routing
    implementation("io.javalin:javalin-bundle:${javalinVersion}") //+Jackson, Logback and some testing tools
    implementation("io.javalin.community.ssl:ssl-plugin:${javalinVersion}")

    implementation("org.webjars.npm:bootstrap:5.3.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}