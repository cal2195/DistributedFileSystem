package api

import java.io.File
import java.time.Instant

class Cache {
    val cacheMap = HashMap<Int, CachedFile>()

    fun getTimestamp(hash: Int) : Instant? {
        return cacheMap[hash]?.timestamp
    }

    fun readBytes(hash: Int) : ByteArray? {
        return cacheMap[hash]?.file?.readBytes()
    }

    fun appendBytes(path: String, data: ByteArray, timestamp: Instant) {
        val hash = path.hashCode()
        val file = File("cache/$hash")
        file.appendBytes(data)
        cacheMap.put(hash, CachedFile(file, timestamp))
    }

    fun writeBytes(path: String, data: ByteArray, timestamp: Instant) {
        val hash = path.hashCode()
        val file = File("cache/$hash")
        file.writeBytes(data)
        cacheMap.put(hash, CachedFile(file, timestamp))
    }
}

data class CachedFile(val file: File, val timestamp: Instant)