package de.phyrone.libloader.hook

import java.io.File

interface LibHook {
    fun addJarToClassPath(file: File)
}