dependencies {
    compileOnly(project(":supervisor-core"))
    compileOnly(project(":supervisor-reflection"))
    compileOnly(project(":supervisor-repository"))
    compileOnly(project(":supervisor-adapter"))

    compileOnly("com.google.code.gson:gson:2.10.1")
}