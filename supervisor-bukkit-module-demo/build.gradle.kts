repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":supervisor-core"))
    implementation(project(":supervisor-loader"))
    implementation(project(":supervisor-bukkit"))


    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}