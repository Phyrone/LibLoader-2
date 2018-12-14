package de.phyrone.libloader.util.mavenruntimeloader

import de.phyrone.libloader.hook.LibHook
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun initMaven(
        hook: LibHook,
        downloadURL: String = "https://static.phyrone.de/download/LIB/MavenResolver-With-Dependencies-13.12.18.jar",
        mvnJarFile: File = File("libs/", "MvnLoader.jar")
) {
    if (!mvnJarFile.exists()) {
        val con = URL(downloadURL).openConnection()
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
        val inSteam = con.getInputStream()
        mvnJarFile.parentFile.mkdirs()
        Files.copy(inSteam, mvnJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
    hook.addJarToClassPath(mvnJarFile)
}