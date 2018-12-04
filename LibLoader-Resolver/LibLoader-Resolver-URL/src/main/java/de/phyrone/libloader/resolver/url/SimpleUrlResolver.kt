package de.phyrone.libloader.resolver.url

import de.phyrone.libloader.resolver.LibResolver
import de.phyrone.libloader.resolver.UnresolvedDependencyExeption
import java.io.File
import java.io.IOError
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object SimpleUrlResolver : LibResolver {

    override fun resolve(string: String): Array<File>? {
        try {
            val file = File.createTempFile("LibLoader-", ".jar")

            val url = URL(string)
            val connection = url.openConnection()
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0")
            Files.copy(connection.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            file.deleteOnExit()
            return arrayOf(file)
        } catch (e: IOException) {
            throw UnresolvedDependencyExeption("Url Malformed")
        } catch (e: MalformedURLException) {
            throw UnresolvedDependencyExeption(e.message ?: "")
        }
    }
}