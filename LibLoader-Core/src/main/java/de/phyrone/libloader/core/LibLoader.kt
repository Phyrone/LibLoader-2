package de.phyrone.libloader.core

import de.phyrone.libloader.hook.LibHook
import de.phyrone.libloader.resolver.LibResolver
import de.phyrone.libloader.resolver.UnresolvedDependencyExeption
import java.io.File
import java.util.concurrent.FutureTask
import java.util.logging.Logger
import kotlin.system.measureTimeMillis

open class LibLoader(private val hook: LibHook, vararg resolvers: LibResolver = arrayOf(), val logger: Logger? = null) {
    val resolvers = arrayListOf(*resolvers)

    /**
     * add a Resolver to the Resolvers list
     *
     * this is made for add Resolvers add Later for example Maven is sometimes a bit large so you can add this later
     *
     * @param resolver the resolver
     */
    fun addResolver(resolver: LibResolver) {
        resolvers.add(resolver)
    }

    /**
     * resolves a dependency at and add them to the
     * @param dependency is the dependency string Usual its an url or a gradle/maven coordinate (ex. "com.google.guava:guava:27.0-jre")
     * @throws UnresolvedDependencyExeption if no resolver found a Dependency
     */
    fun require(dependency: String) {
        resolve(dependency).forEach { file ->
            hook.addJarToClassPath(file)
        }
    }

    /**
     * resolves a dependency at and return the class-files (usual jar)
     * @param dependency is the dependency string Usual its an url or a gradle/maven coordinate (ex. "com.google.guava:guava:27.0-jre")
     * @throws UnresolvedDependencyExeption if no resolver found a Dependency
     */
    fun resolve(dependency: String): Array<File> {
        logger?.fine("Resolve $dependency...")
        val task = FutureTask<Array<File>>() {
            for (resolver in resolvers) {
                try {
                    val ret = resolver.resolve(dependency)
                    if (ret.isNullOrEmpty()) {
                        throw UnresolvedDependencyExeption("Wrong Result")
                    } else {

                        return@FutureTask ret
                    }
                } catch (e: UnresolvedDependencyExeption) {
                    if (!e.message.isNullOrBlank())
                        logger?.warning("($dependency) a Resolver Failed with message \"" + e.message + "\"")
                    continue
                }
            }
            throw UnresolvedDependencyExeption("No Resolver found a Dependency")
        }
        val time = measureTimeMillis {
            task.run()
        }
        logger?.finer("Resolved $dependency (took $time ms)")
        return task.get()
    }


}

