plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

group = "com.vertmix.supervisor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")

}


subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")

    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")


        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                // Use either shadowJar or jar for the artifact, depending on your project's requirements
                val shadowJarTask = tasks.findByName("shadowJar") as? com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
                if (shadowJarTask != null) {
                    artifact(shadowJarTask.archiveFile.get().asFile) {
                        classifier = null
                    }
                } else {
                    from(components["java"])
                }

                // Set the groupId, version, and artifactId dynamically
                groupId = project.group.toString()
                // Use the project directory's name for the artifactId
                artifactId = project.name
                version = project.version.toString()

                // Optional: Print out the artifact details for confirmation
                println("Configuring publication for ${project.name} with artifactId ${project.name}")
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.test {
        useJUnitPlatform()
    }
}
