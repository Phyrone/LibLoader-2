package de.phyrone.libloader.resolver.cached

import de.phyrone.libloader.resolver.LibResolver
import de.phyrone.libloader.resolver.UnresolvedDependencyExeption
import org.apache.commons.codec.digest.DigestUtils
import java.io.*
import java.nio.file.Files

class CachedResolver(vararg resolvers: LibResolver, private val cacheFolder: File = File(".", "libs/")) : LibResolver {
    val resolverList = arrayListOf(*resolvers)
    val data = loadCacheData()
    fun addResolver(resolver: LibResolver) {
        resolverList.add(resolver)
    }

    val cacheDataFile = File(cacheFolder.path, "CacheData")
    override fun resolve(string: String): Array<File>? {
        fun loadCache(): Array<File> {
            val ret = ArrayList<File>()
            data[string]?.forEach { hash ->
                ret.add(hashToCacheFile(hash))
            }
            return ret.toTypedArray()
        }
        return if (data.containsKey(string) || valiadateCached(data[string]
                        ?: throw NullPointerException("this Should be Thrown (Multithreaded?)"))) {
            loadCache()
        } else {
            addToCache(string, resolveOrigin(string))
            loadCache()
        }
    }

    private fun loadCacheData(): CacheData {
        try {
            if (cacheDataFile.exists() && cacheDataFile.isFile) {
                val objStream = ObjectInputStream(FileInputStream(cacheDataFile))
                val ret = objStream.readObject() as CacheData
                objStream.close()
                return ret
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return CacheData()
    }

    private fun saveCacheData() {
        val outStream = ObjectOutputStream(FileOutputStream(cacheDataFile))
        outStream.writeObject(data)
        outStream.close()
    }

    private fun valiadateCached(hashes: Array<String>): Boolean {
        hashes.forEach { hash ->
            val file = hashToCacheFile(hash)
            if (!file.exists() || file.isDirectory || fileToHash(file) != hash) {
                return false
            }
        }
        return true
    }

    private fun addToCache(name: String, files: Array<File>) {
        val names = ArrayList<String>()
        files.forEach { file ->
            val fileHash = fileToHash(file)
            val target = hashToCacheFile(fileHash)
            names.add(fileHash)
            if (target.exists() && fileToHash(target).equals(fileHash, true)) {
                /* Already Exists */
                return
            } else {
                Files.copy(file.toPath(), target.toPath())
            }
        }
        data[name] = names.toTypedArray()
        saveCacheData()
    }


    private fun hashToCacheFile(hash: String) = File(cacheFolder.path, "$hash.jar")
    private fun resolveOrigin(dependency: String): Array<File> {
        for (resolver in resolverList) {
            try {
                val ret = resolver.resolve(dependency)
                if (ret.isNullOrEmpty()) {
                    throw UnresolvedDependencyExeption("Wrong Result")
                } else {
                    return ret
                }
            } catch (e: UnresolvedDependencyExeption) {
                continue
            }
        }
        throw UnresolvedDependencyExeption("No Resolver found a Dependency")
    }

    fun fileToHash(file: File) = DigestUtils.sha512Hex(FileInputStream(file))!!

}

class CacheData : HashMap<String, Array<String>>(), Serializable