package de.phyrone.libloader.resolver.url

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import de.phyrone.libloader.resolver.LibResolver
import de.phyrone.libloader.resolver.UnresolvedDependencyExeption
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

val hashFunction: HashFunction = Hashing.sha512()

object SimpleUrlResolver : UrlResolver(File(System.getProperty("user.home", "/") + "/.LibLoader", "SimpleUrlResolver"))

open class UrlResolver(private val cacheFolder: File) : LibResolver {

    override fun resolve(string: String): Array<File>? {
        try {
            val file = File(cacheFolder.path, hashFunction.hashString(string, StandardCharsets.UTF_8).toString() + ".jar")
            if (!file.exists()) {
                val url = URL(string)
                val connection = url.openConnection()
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0")
                Files.copy(connection.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            return arrayOf(file)
        } catch (e: IOException) {
            throw UnresolvedDependencyExeption("Url Malformed")
        } catch (e: MalformedURLException) {
            throw UnresolvedDependencyExeption(e.message ?: "")
        }
    }
}