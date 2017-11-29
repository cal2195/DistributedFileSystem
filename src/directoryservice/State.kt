package directoryservice

import directoryservice.filesystem.File
import directoryservice.network.ConnectionAddress
import java.io.Serializable
import kotlin.collections.HashMap

class State: Serializable {
    val root = File("/", true)
    val servers = HashMap<Int, ConnectionAddress>()
}
