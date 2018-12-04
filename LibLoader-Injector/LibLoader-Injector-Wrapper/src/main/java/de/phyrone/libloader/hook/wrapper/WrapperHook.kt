package de.phyrone.libloader.hook.wrapper

import de.phyrone.libloader.hook.LibHook
import java.io.File
import java.net.URL
import java.net.URLClassLoader

class LibClassloader(val parentClassloader: ClassLoader = LibClassloader::class.java.classLoader) : URLClassLoader(arrayOf(), parentClassloader), LibHook {
    override fun addJarToClassPath(file: File) {
        addURL(file.toURI().toURL())
    }

    fun newAppClassLoader(source: URL): LibAppClassloader = LibAppClassloader(this, source)

    fun newAppClassLoader(source: String): LibAppClassloader = newAppClassLoader(parentClassloader.getResource(source))

    fun newAppClassLoader(sourceFile: File): LibAppClassloader = newAppClassLoader(sourceFile.toURI().toURL())


}

class LibAppClassloader(libClassloader: LibClassloader, appSource: URL) : URLClassLoader(arrayOf(appSource), libClassloader) {
    fun psvm(classPath: String, vararg args: String = arrayOf()) {
        Class.forName(classPath, true, this)
                .getDeclaredMethod("main", Array<String>::class.java)
                .invoke(null, args)
    }

    fun instanceNewClass(name: String, vararg args: Any): Any {
        val inClass = Class.forName(name, true, this)
        val classes = args.map { it::class.java }.toTypedArray()
        val contructor = inClass.getDeclaredConstructor(*classes)
        return contructor.newInstance(*args)
    }
}
