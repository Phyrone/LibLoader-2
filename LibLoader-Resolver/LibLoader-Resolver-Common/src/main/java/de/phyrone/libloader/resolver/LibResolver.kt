package de.phyrone.libloader.resolver

import java.io.File

interface LibResolver {
    @Throws(UnresolvedDependencyExeption::class)
    fun resolve(string: String): Array<File>?
}