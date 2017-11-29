package directoryservice

import directoryservice.filesystem.File
import java.io.Serializable
import java.net.InetAddress
import kotlin.collections.HashMap

class State: Serializable {
    val root = File("/", true)
    val servers = HashMap<Int, InetAddress>()
}
