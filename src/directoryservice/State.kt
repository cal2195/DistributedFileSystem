package directoryservice

import directoryservice.filesystem.File
import directoryservice.network.ConnectionAddress
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

class State: Serializable {
    val root = File("/", true)
    val servers = ConcurrentHashMap<Int, ConnectionAddress>()
}
