import client.Client
import directoryservice.DirectoryService
import directoryservice.network.ConnectionAddress
import directoryservice.network.Connections
import fileserver.Node

val SERVER_PORT = 8768

fun main(args: Array<String>) {
    if (args[0] == "dir") {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Saving state...")
            DirectoryService.saveState()
        })
        Connections.listen(SERVER_PORT)
    }

    if (args[0] == "file") {
        val fileServer = Node(args[1], args[2], args[3].toInt())
    }

    if (args[0] == "fuse") {
        val client = Client()
        client.init(args[1], ConnectionAddress(args[2], SERVER_PORT))
    }
}