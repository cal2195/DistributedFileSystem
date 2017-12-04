package api

import directoryservice.filesystem.Attr
import directoryservice.network.*
import fileserver.packets.*
import java.io.File
import java.util.*

class FileSystemApi(val dirServerAddress: ConnectionAddress) {

    val cache = Cache()

    fun getAttr(path: String): Attr? {
        val dirResponse = Connection.dirRequest(DirAttrRequest(path), dirServerAddress) as DirAttrResponse
        return dirResponse.attr
    }

    fun read(path: String): ByteArray {
        val dirResponse = Connection.dirRequest(DirReadRequest(path, false), dirServerAddress) as DirReadResponse

        val cached = cache.getTimestamp(dirResponse.hash)

        if (cached == null || cached.isBefore(dirResponse.attr.timestamp)) {
            println("Cache miss for $path")
            val address = Helper.pickAddress(dirResponse.servers) ?: return kotlin.ByteArray(0)
            val response = Connection.nodeRequest(NodeReadRequest(dirResponse.hash), address) as NodeReadResponse
            cache.writeBytes(path, response.data, dirResponse.attr.timestamp)
            return response.data
        }

        println("Cache hit for $path")
        return cache.readBytes(dirResponse.hash) ?: ByteArray(0)
    }

    fun create(path: String) {
        val file = File("cache$path")
        file.parentFile.mkdirs()
        file.createNewFile()
    }

    fun write(path: String, data: ByteArray) {
        val file = File("cache$path")
        file.parentFile.mkdirs()
        file.appendBytes(data)
    }

    fun truncate(path: String) {
        val file = File("cache$path")
        file.delete()
    }

    fun close(path: String) {
        val data = File("cache$path").readBytes()
        println("Sending ${data.size} bytes to servers!")
        val dirResponse = Connection.dirRequest(DirWriteRequest(path, false, data.size.toLong()), dirServerAddress) as DirWriteResponse
        for (address in dirResponse.servers.toList().filterNotNull()) {
            Connection.nodeRequest(NodeWriteRequest(dirResponse.hash, data), address) as NodeWriteResponse
        }
    }

    fun rm(path: String) {
        val dirResponse = Connection.dirRequest(DirDeleteRequest(path), dirServerAddress) as DirDeleteResponse
        for (address in dirResponse.servers.toList().filterNotNull()) {
            Connection.nodeRequest(NodeDeleteRequest(dirResponse.hash), address) as NodeDeleteResponse
        }
    }

    fun readdir(path: String): ArrayList<String> {
        val response = Connection.dirRequest(DirReadRequest(path, true), dirServerAddress) as DirListResponse
        return response.files
    }

    fun mkdir(path: String) {
        Connection.dirRequest(DirWriteRequest(path, true, 0), dirServerAddress) as DirWriteResponse
    }

    fun rmdir(path: String) {
        Connection.dirRequest(DirDeleteRequest(path), dirServerAddress) as DirDeleteResponse
    }
}