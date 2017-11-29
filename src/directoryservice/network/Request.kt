package directoryservice.network

import directoryservice.DirectoryService
import directoryservice.filesystem.Hashing
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.time.Instant

class Request(var clientSocket: Socket) : Runnable {

    private var inputStream = ObjectInputStream(clientSocket.getInputStream())
    private var outputStream = ObjectOutputStream(clientSocket.getOutputStream())

    override fun run() {
        println("Started request for client ${clientSocket.remoteSocketAddress}")
        listen()
    }

    private fun listen() {
        var packet = inputStream.readObject()

        when (packet) {
            is JoinRequest -> processJoin(packet)
            is ReadRequest -> processRead(packet)
            is WriteRequest -> processWrite(packet)
            is DeleteRequest -> processDelete(packet)
        }
    }

    private fun processJoin(packet: JoinRequest) {
        DirectoryService.state.servers.put(packet.key, packet.address)
        respond(JoinResponse(true))
    }

    private fun processRead(packet: ReadRequest) {
        if (packet.isDir) {
            val node = DirectoryService.state.root.findNode(packet.path, packet.isDir)
            val list = ArrayList(node.children.keys)
            respond(ListResponse(list))
        } else {
            val key = packet.path.hashCode()
            val servers = Hashing.getClosest(key)
            respond(ReadResponse(key, servers))
        }
    }

    private fun processWrite(packet: WriteRequest) {
        val key = packet.path.hashCode()
        val servers = Hashing.getClosest(key)
        // Create file/dir in tree and update timestamp
        DirectoryService.state.root.findNode(packet.path, packet.isDir).timestamp = Instant.now()
        respond(WriteResponse(key, servers))
    }

    private fun processDelete(packet: DeleteRequest) {
        val parentPath = packet.path.subSequence(0, packet.path.lastIndexOf("/")) as String
        val child = packet.path.subSequence(packet.path.lastIndexOf("/"), packet.path.length)
        val parent = DirectoryService.state.root.findNode(parentPath, true)
        parent.children.remove(child)

        val key = packet.path.hashCode()
        val servers = Hashing.getClosest(key)

        respond(DeleteResponse(key, servers))
    }

    private fun respond(packet: Any) {
        outputStream.writeObject(packet)
        outputStream.flush()
        outputStream.close()
    }
}
