repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":supervisor-core"))
    implementation(project(":supervisor-reflection"))
    implementation(project(":supervisor-loader"))
    implementation(project(":supervisor-bukkit-item"))
    implementation(project(":supervisor-configuration"))
    implementation(project(":supervisor-configuration-yml"))
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")

    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}