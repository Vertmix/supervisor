rootProject.name = "supervisor"

rootDir.listFiles()
    ?.filter { it.isDirectory && it.name.startsWith("supervisor") }
    ?.forEach { include(it.name) }
