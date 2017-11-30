package client

import api.FileSystemApi
import directoryservice.network.ConnectionAddress
import java.io.File

class Client {
    fun init(path: String, address: ConnectionAddress) {
        var mountPoint = File(path)
        mountPoint.mkdir()

        var cache = File("cache")
        cache.mkdir()

        var fuse = Fuse(FileSystemApi(address))
        fuse.mount(mountPoint)
    }
}