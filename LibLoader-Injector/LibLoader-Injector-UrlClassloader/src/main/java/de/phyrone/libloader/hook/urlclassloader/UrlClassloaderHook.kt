package de.phyrone.libloader.hook.urlclassloader

import de.phyrone.libloader.hook.LibHook
import java.io.File
import java.net.URL
import java.net.URLClassLoader

class UrlClassloaderHook(val classloader: URLClassLoader) : LibHook {

    override fun addJarToClassPath(file: File) {
        method.invoke(classloader, file.toURI().toURL())
    }

    companion object {
        private val method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java).also {
            it.isAccessible = true
        }

        @Deprecated("not work in java 9 and above")
        fun fromSystemClassloader(): UrlClassloaderHook {
            return UrlClassloaderHook(ClassLoader.getSystemClassLoader() as URLClassLoader)
        }

        fun fromParentClassloader(clazz: Class<*> = UrlClassloaderHook::class.java): UrlClassloaderHook {
            return UrlClassloaderHook(clazz.classLoader as URLClassLoader)
        }

        fun fromThreadContextClassloader(): URLClassLoader {
            return Thread.currentThread().contextClassLoader as URLClassLoader
        }

    }
}