dependencies {
    compileOnly(project(":supervisor-core"))
    compileOnly(project(":supervisor-reflection"))
    compileOnly(project(":supervisor-repository"))
    compileOnly(project(":supervisor-repository-redis"))

    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.mongodb:mongodb-driver-sync:4.7.0")
    compileOnly("redis.clients:jedis:5.2.0")

}